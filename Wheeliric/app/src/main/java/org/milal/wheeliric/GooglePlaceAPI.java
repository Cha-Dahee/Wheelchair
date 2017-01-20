package org.milal.wheeliric;

import android.os.AsyncTask;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yoojung on 2017-01-17.
 * 위치를 이용해 반경안에 있는 장소를 찾아주는 Google API
 * TMap이 더 적합하다 판단하여 사용하지 않음.
 */

public class GooglePlaceAPI extends AsyncTask<Double, Void, List<Facility>> {

    private double latitude;
    private double longitude;
    private double radius;
    //private static final String types = "atm|bakery|bank|bar|beauty_salon|book_store|bus_station|cafe|church|city_hall|clothing_store|convenience_store|dentist|department_store|doctor|electronics_store|fire_station|florist|food|grocery_or_supermarket|gym|hair_care|health|hospital|jewelry_store|laundry|library|local_government_office|meal_takeaway|movie_theater|park|parking|pet_store|pharmacy|place_of_worship|police|post_office|restaurant|school|shoe_store|shopping_mall|store|subway_station|train_station|university|zoo";
    private static final String types = "food";
    private static final String APIUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
    private static final String APIKey = "AIzaSyBLCzWp7yh-mkfoZ5-Mwt9iID8la5_rvxM";

    StringBuilder mResponseBuilder = new StringBuilder();
    List<Facility> mList = new ArrayList<>();

    @Override
    protected List<Facility> doInBackground(Double... values) {
        latitude = values[0];
        longitude = values[1];
        radius = values[2];
        HttpURLConnection connection = null;
        BufferedReader in = null;

        String PLACE_SEARCH_URL = APIUrl + latitude + "," + longitude + "&radius=" + radius + "&types=" + types +"&language=ko&key=" + APIKey;

        try {
            URL url = new URL(PLACE_SEARCH_URL);
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

            for (int i = 0; i < jArr.length(); i++) {
                JSONObject result = jArr.getJSONObject(i);
                JSONObject geo = result.getJSONObject("geometry");
                JSONObject location = geo.getJSONObject("location");

                String lat = location.getString("lat");
                String lng = location.getString("lng");
                String name = result.getString("name");
                String id = result.getString("place_id");
                String vin = result.getString("vicinity");
                mList.add(new Facility(name, id, vin, Double.valueOf(lat), Double.valueOf(lng)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mList;
    }
}