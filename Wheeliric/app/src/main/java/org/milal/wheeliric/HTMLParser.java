package org.milal.wheeliric;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;


/**
 * Created by Yoojung on 2017-07-12.
 */

public class HTMLParser extends AsyncTask<String, Void, ArrayList<URLObject>>{

    @Override
    protected ArrayList<URLObject> doInBackground(String... params){

        String text = params[0];
        String subText = "";

        try {
            subText = URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String URL = "https://www.google.co.kr/search?q=" + subText + "&site=webhp&source=lnms&tbm=isch&sa=X";
        Document doc;
        Elements div;
        ArrayList<URLObject> objects = new ArrayList<>();

         try {
            doc = Jsoup.connect(URL).get();
            div = doc.select("div[class=rg_meta notranslate]");

            for(Element e : div){
                JSONObject object = (JSONObject) new JSONTokener(e.text()).nextValue();
                objects.add(new URLObject(object.getString("ou"), object.getString("ru")));
            }

         } catch (IOException e) {
            e.printStackTrace();
         } catch (JSONException e) {
            e.printStackTrace();
         }

        return objects;
    }
}

class URLObject {

    private String image;
    private String url;

    public URLObject(String image, String url){
        this.image = image;
        this.url = url;
    }

    public String getImage(){return image;}
    public String getUrl(){return url;}
}