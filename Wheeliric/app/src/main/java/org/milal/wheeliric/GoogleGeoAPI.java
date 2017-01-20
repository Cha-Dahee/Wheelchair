package org.milal.wheeliric;

import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Yoojung on 2017-01-18.
 * 위치를 주소로 변환시켜주는 역지오코딩 Google API
 */

public class GoogleGeoAPI extends AsyncTask<LatLng, Void, String> {

    private Context mContext;
    private LatLng myPosition;
    private String vin;
    private static final String APIUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=";
    private static final String APIKey = "AIzaSyBLCzWp7yh-mkfoZ5-Mwt9iID8la5_rvxM";

    @Override
    protected String doInBackground(LatLng... values){
        myPosition = values[0];
        double latitude = myPosition.latitude;
        double longitude = myPosition.longitude;

        HttpURLConnection connection = null;
        BufferedReader in = null;
        StringBuilder mResponseBuilder = new StringBuilder();

        String GEO_URL = APIUrl + latitude + "," + longitude + "&language=ko&key=" + APIKey;

        try {
            URL url = new URL(GEO_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            in = new BufferedReader(new InputStreamReader(stream));
            String inputLine = "";

            while ((inputLine = in.readLine()) != null) {
                mResponseBuilder.append(inputLine);
            }
            in.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONArray jArr;
            JSONObject jObj;

            jObj = new JSONObject(mResponseBuilder.toString());
            jArr = jObj.getJSONArray("results");

            JSONObject result = jArr.getJSONObject(0);
            vin = result.getString("formatted_address");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return vin;
    }
}

