package org.milal.wheeliric;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class DaumCafeList extends AsyncTask<Void, Void, ArrayAdapter<String>> {
    String url;
    String keyWord;
    private ArrayList<String> list;

    private ProgressDialog progressDialog;
    private Context context;
    private ListView listView;
    private TextView textView;
    private ArrayAdapter<String> adapter;

    public DaumCafeList(Context context, ListView listView, TextView textView, String keyWord){
        this.context = context;
        this.keyWord = keyWord;
        this.listView = listView;
        this.textView = textView;
        progressDialog = new ProgressDialog(context);
        adapter = null;
    }

    @Override
    protected void onPreExecute() {

        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("로딩중입니다..");
        progressDialog.show();

        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayAdapter<String> result) {
        progressDialog.dismiss();

        if(list.size() != 0) {
            result = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, list);
            result.notifyDataSetChanged();
        } else{
            textView.setText("검색결과가 없습니다.");
        }
        listView.setDividerHeight(3);
        listView.setOnItemClickListener(itemClickListenerOfSearchResult);
        listView.invalidate();
        listView.setAdapter(result);

        super.onPostExecute(result);
    }

    //스레드 본체
    @Override
    protected ArrayAdapter<String> doInBackground(Void... voids) {
        //한글이 깨지지 않게 하기 위해
        try {
            String encodedK = "\"" + URLEncoder.encode(keyWord, "utf-8") + "\"+";
            String defaultOp = "\"" + URLEncoder.encode("휠체어", "utf-8") + "\"";
            StringBuffer buffer = new StringBuffer();
            buffer.append("http://apis.daum.net/search/cafe?result=20&");
            //한글일 경우 인코딩 필요!(영어로 가정한다)
            buffer.append("q="+encodedK+defaultOp);
            buffer.append("&apikey=d847f0a73b32bb8fa47db90e8d71f7dd");
            buffer.append("&output=json");
            url = buffer.toString();
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        HttpURLConnection conn = null;
        StringBuilder builder = new StringBuilder();
        try {
            URL url=new URL(this.url);
            conn= (HttpURLConnection)url.openConnection();
            if(conn != null){//정상접속이 되었다면
                conn.setConnectTimeout(10000);//최대 대기시간10초
                conn.setUseCaches(false);//캐쉬사용안함
                if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
                    //InputStreamReader 객체 얻어오기
                    InputStreamReader isr=
                            new InputStreamReader(conn.getInputStream());
                    BufferedReader br=new BufferedReader(isr);
                    //반복문 돌면서 읽어오기
                    while(true){
                        String line=br.readLine();
                        if(line==null)break;
                        //읽어온 문자열을 객체에 저장
                        builder.append(line);
                    }
                    br.close();
                }//if
            }//if

            Message msg= new Message();
            msg.what=0; //성공
            msg.obj = builder.toString();
            getCafeInfo(msg);

        } catch (Exception e) {
            // TODO Auto-generated catch block

            Log.e("post 전송중 에러!", e.getMessage());
            Message msg= new Message();
            msg.what=1; //실패
            msg.obj = "데이터를 받아올 수 없습니다.";
            getCafeInfo(msg);

        }finally{
            conn.disconnect(); //접속 종료
        }
        return adapter;
    }

    protected void getCafeInfo(android.os.Message msg){

        list = new ArrayList<String>();
        switch(msg.what){
            case 0 : //success
                //json문자열을 ㅡ읽어오기
                String jsonStr = (String)msg.obj;
                try{
                    //문자열을 json 객체로 변환
                    //1. channel이라는 키값으로 {} jsonObject가 들어있다)
                    //2. jsonObject안에는 item이라는 키값으로 [] jsonArray 벨류값을 가지고 있다.
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    //1.
                    JSONObject channel = jsonObj.getJSONObject("channel");
                    //2.
                    JSONArray items = channel.getJSONArray("item");
                    //3.반복문 돌면서 필요한 정보만 얻어온다.
                    for(int i=0 ; i<items.length() ; i++){
                        //4. 검색결과 값을 얻어온다.
                        JSONObject tmp = items.getJSONObject(i);
                        String cafeName = tmp.getString("cafeName");

                        if(!(cafeName.equalsIgnoreCase("휠체어배낭여행(장애인여행)")||cafeName.equalsIgnoreCase("휠체어로 세계로...")))
                            continue;
                        String title = tmp.getString("title");
                        title = removeHtmlTag(title);
                        String link = tmp.getString("link");
                        list.add(title + "\n" + link + "\n" + cafeName);
                    }
                }catch (Exception e) {

                }

                break;

            case 1 : //fail

                break;
        }
    }

    private String removeHtmlTag(String content) {

        content = content.replaceAll("&lt;b&gt;", " ");
        content = content.replaceAll("&lt;/b&gt;", " ");

        return content;
    }

    private AdapterView.OnItemClickListener itemClickListenerOfSearchResult = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View clickedView, int position, long id) {
            String str = ((TextView)clickedView).getText().toString();
            String cafeLink = str.split("\n")[1];
            Intent CafePosting = new Intent(Intent.ACTION_VIEW, Uri.parse(cafeLink));
            // Intent CafePosting = new Intent(getApplicationContext(), ShowCafePosting.class);
            context.startActivity(CafePosting);
        }
    };

}