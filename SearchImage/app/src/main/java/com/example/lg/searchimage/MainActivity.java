package com.example.lg.searchimage;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends Activity {

    String clientId = "IANZ3_J3jzB5YFBHOPXk";//애플리케이션 클라이언트 아이디값";
    String clientSecret = "Np7sHkIaiA";//애플리케이션 클라이언트 시크릿값";
    String responseS ="";
    String imageUrl;
    int i;
    private com.google.android.gms.common.api.GoogleApiClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageA = (ImageView) findViewById(R.id.haeun);

        try {
            responseS =  new AsyncTask1().execute(clientId, clientSecret).get();
            JSONObject jsonObj = new JSONObject(responseS);
            JSONArray items = jsonObj.getJSONArray("items");

            Log.v(String.valueOf(items.length()),"");

            //for (i = 0; i < items.length(); i++) {
                JSONObject obj = items.getJSONObject(0);
                imageUrl = obj.getString("link");
               // Log.v(String.valueOf(i),imageUrl);
           // }

            Picasso.with(this).load(imageUrl).into(imageA);

            } catch (Exception e) {
        }
    }
}