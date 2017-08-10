package org.milal.wheeliric;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class NaverBlogList extends AsyncTask<String, Void, ArrayAdapter<String>> {

    final static String clientId = "RFfoSsVheD6DHYT0PvR3";//애플리케이션 클라이언트 아이디값";
    final static String clientSecret = "aG5dJvvTyH";//애플리케이션 클라이언트 시크릿값";\

    private ProgressDialog progressDialog;
    private Context context;
    private ListView listView;
    private TextView textView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> blogresult;

    int startNum = 1;//4번
    String displayNum = "50";//3번
    String responseS = "";
    String apiURL = "";
    String response = "";
    String strCookie = "";
    int i =0;

    public NaverBlogList(Context context, ListView listView, TextView textView){
        this.context = context;
        this.listView = listView;
        this.textView = textView;
        progressDialog = new ProgressDialog(context);
        adapter = null;
    }

    @Override
    protected void onPreExecute() {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("로딩중입니다..");
        progressDialog.show();

        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayAdapter<String> result) {
        progressDialog.dismiss();

        if(blogresult.size() != 0) {
            result = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, blogresult);
            result.notifyDataSetChanged();
        } else{
            textView.setText("검색결과가 없습니다.");
        }

        listView.setDividerHeight(3);
        listView.setOnItemClickListener(itemClickListenerOfSearchResult);
        listView.invalidate();
        listView.setAdapter(result);
        int height = blogresult.size();

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = 150 * height;
        listView.setLayoutParams(params);
        listView.requestLayout();

        super.onPostExecute(result);
    }

    @Override
    protected void onCancelled(ArrayAdapter<String> result){
        if(progressDialog != null)
            progressDialog.dismiss();

        super.onCancelled();
    }

    @Override
    protected ArrayAdapter<String> doInBackground(String...strings) {

        blogresult = new ArrayList<String>();

        try {
            //'검색어+주차장' 을 돌리고, '검색어+화장실'을 돌려서 나오는 링크들과 일치하는 게 있으면 먼저보여준다.
            // 상위랭크 블로그포스팅 50개를 검사해서, 그 안에 주차장 또는 화장실, 전화번호 라는 말이 있으면 상위랭크로 해주고,
            //그래서 총 5개의 링크만 보여주자

            // 순수한 검색어만 돌려서 나온 링크들 받아오기
            responseS = getJson(clientId, clientSecret, strings[0], displayNum, String.valueOf(startNum)); // 기본데이터(총 data수 같은거) 읽어오기위한!
            JSONObject jsonObj = new JSONObject(responseS);

            ArrayList bloglists = new ArrayList();//순수한 검색어에서 받아온 블로그 링크 리스트(최대 50개)

            String[] array;

            JSONArray items = jsonObj.getJSONArray("items");
            for (i = 0; i < items.length(); i++) {
                JSONObject obj = items.getJSONObject(i);
                if(obj.getString("link").contains("http://blog.naver.com")){
                    Log.v("obj", obj.getString("link"));
                    array = obj.getString("link").split(";"); //link가 logNo을 기준으로 나눠짐
                    array[1] = array[1].substring(6); //6번째부터 마지막까지 잘라서 다시 저장한다.
                    bloglists.add( obj.getString("bloggerlink") + "/"+array[1]);
                } else {
                    bloglists.add(obj.getString("bloggerlink"));
                }
            }

            String[] bloglist = ( String[]) bloglists.toArray( new String[ bloglists.size()]); //결과

            //검색어+주차장 돌려서 링크 받아오기
            responseS = getJson(clientId, clientSecret, strings[0] + ", 주차장", displayNum, String.valueOf(startNum)); // 기본데이터(총 data수 같은거) 읽어오기위한!
            JSONObject jsonObj2 = new JSONObject(responseS);

            ArrayList bloglists_park = new ArrayList();//주차장을 포함한 검색어에서 받아온 블로그 링크 리스트(최대 50개)

            JSONArray items_ = jsonObj2.getJSONArray("items");
            for (i = 0; i < items_.length(); i++) {
                JSONObject obj = items_.getJSONObject(i);
                if(obj.getString("link").contains("http://blog.naver.com")) {
                    array = obj.getString("link").split(";"); //link가 logNo을 기준으로 나눠짐
                    array[1] = array[1].substring(6); //6번째부터 마지막까지 잘라서 다시 저장한다.
                    bloglists_park.add(obj.getString("bloggerlink") + "/" + array[1]);
                } else {
                    bloglists_park.add(obj.getString("bloggerlink"));
                }
            }

            String[] bloglist_ = ( String[]) bloglists_park.toArray( new String[bloglists_park.size()]); //결과
            //두 배열에서 겹치는 거가 있으면 먼저 보여준다.

            for(i = 0; i < items_.length(); i++){
                if(bloglist_[i].equals(bloglist)){
                    blogresult.add(bloglist_[i]);

                }
                if(blogresult.size()==5)
                    break;
            }

            if(blogresult.size()<5)
                for(i=0;i<5;i++) {
                    blogresult.add(bloglist[i]);
                    if(blogresult.size()==5)
                        break;
                }

            /*
            blogresult_ = ( String[]) blogresult.toArray( new String[blogresult.size()]);

            for(i=0;i<5;i++) {
                blogresult_[i] = blogresult_[i].replaceAll("<[^>]*>", " ");
                Log.v("list", blogresult_[i]);
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
        return adapter;
    }


    public String getJson(String ...strings) {
        try {
            String text = URLEncoder.encode(strings[2], "UTF-8");//strings[2]==한글검색어

            apiURL = "https://openapi.naver.com/v1/search/blog.json?query=" + text + "&display=" + strings[3] + "&start=" + strings[4];//strings[3]==displayNum,strings[4]==startNum
            URL url = new URL(apiURL);

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-Naver-Client-Id", strings[0]);//strings[0] == client-id
            conn.setRequestProperty("X-Naver-Client-Secret", strings[1]);//strings[1]==client-secret
            conn.setDoInput(true); // 읽기모드 지정

            strCookie = conn.getHeaderField("Set-Cookie"); //쿠키데이터 보관

            BufferedReader reader;
            StringBuilder builder = new StringBuilder(); //문자열을 담기 위한 객체

            if (conn.getResponseCode() == 200) {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream())); //문자열 셋 세팅
            } else {
                reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            String line;//한줄씩 읽어오는걸 임시 저장

            while ((line = reader.readLine()) != null) {
                builder.append(line + "\n");
            }

            response = builder.toString(); //최종 string
            //Log.v("글복사",response);
        } catch (Exception e) {
        }
        return response;
    }

    private AdapterView.OnItemClickListener itemClickListenerOfSearchResult = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View clickedView, int position, long id) {
            String str = ((TextView)clickedView).getText().toString();
            Intent CafePosting = new Intent(Intent.ACTION_VIEW, Uri.parse(str));
            // Intent CafePosting = new Intent(getApplicationContext(), ShowCafePosting.class);
            context.startActivity(CafePosting);
        }
    };
}