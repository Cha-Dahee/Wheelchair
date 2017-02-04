package com.example.lg.searchblog;


import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NaverSearchAPI extends Activity {

    String clientId = "IANZ3_J3jzB5YFBHOPXk";//애플리케이션 클라이언트 아이디값";
    String clientSecret = "Np7sHkIaiA";//애플리케이션 클라이언트 시크릿값";

    String searchName = "";//2번
    int startNum=1;//4번
    String displayNum = "100";//3번
    int totalData; //전체 글 수
    int max;

    String responseS ="";
    String resultTt="";
    String resultBl="";

    int i,j;

    private com.google.android.gms.common.api.GoogleApiClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textA = (TextView) findViewById(R.id.dd);
        textA.setMovementMethod(new ScrollingMovementMethod());

        searchName = "갈비";
        try {
            responseS = new AsyncTask1().execute(clientId, clientSecret, searchName, displayNum, String.valueOf(startNum)).get(); // 기본데이터(총 data수 같은거) 읽어오기위한!
            JSONObject jsonObj = new JSONObject(responseS);
            totalData = Integer.valueOf(jsonObj.getString("total"));
            Log.v("totalData int형",String.valueOf(totalData));

            max = totalData/100;
            Log.v("max값",String.valueOf(max));
            if(max>11)  //데이터가 1100개 이상 나오면 그냥 1100개만 있다고 가정
                max=11;
            Log.v("바뀐max값",String.valueOf(max));

            /** 위에만든 기본포맷으로 돌린것, startNum=1, displayNum=100*/
            JSONArray items = jsonObj.getJSONArray("items");
            for (i = 0; i < items.length(); i++) {
                JSONObject obj = items.getJSONObject(i);
                isThereData(obj);
                if(!resultTt.equals(""))
                    break;
            }
            /**기본 이상이 필요할때!(startNum이 100부터!)*/
            for(j=1;j<max;j++) {

                startNum=100*j;
                responseS = new AsyncTask1().execute(clientId, clientSecret, searchName, displayNum, String.valueOf(startNum)).get();
                //Log.v("responseS값",responseS);

                JSONObject obj = new JSONObject(responseS);
                items = obj.getJSONArray("items");

                for (i = 0; i < items.length(); i++) {
                    JSONObject jObj = items.getJSONObject(i);
                    isThereData(jObj);
                    if(!resultTt.equals(""))
                        break;
                }
                Log.v("for문 돈 수",String.valueOf(j));
                if(!resultTt.equals(""))
                    break;
            }

            Log.v("resultTt",resultTt);
            Log.v("??",String.valueOf(resultTt.equals("")));

            if(resultTt.equals("")){
                textA.setText("검색값이 없습니다");
                Log.v("if문","검색값 없음");
            }
            else{
                textA.setText("Title: "+String.valueOf(resultTt)+"\n"+"Link: "+String.valueOf(resultBl)+"\n");
            }
        } catch (Exception e) {
        }
    }

    private void isThereData(JSONObject obj) throws JSONException {

        if(obj.getString("bloggerlink").equals("http://blog.naver.com/love2jho")){
            resultTt = obj.getString("title");
            resultBl = obj.getString("link");
        }
        else if(obj.getString("bloggerlink").equals("http://blog.naver.com/pinkdbswn123")){
            resultTt = obj.getString("title");
            resultBl = obj.getString("link");
        }
        else if(obj.getString("bloggerlink").equals("http://blog.naver.com/hubherb0420")){
            resultTt = obj.getString("title");
            resultBl = obj.getString("link");
        }
    }
}