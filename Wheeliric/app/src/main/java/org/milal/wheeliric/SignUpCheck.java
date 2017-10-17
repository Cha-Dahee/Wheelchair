package org.milal.wheeliric;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * Created by Dahee Joy Cha on 2017-09-18.
 */

public class SignUpCheck extends AsyncTask<String, Void, String> {


    private String DBserverAddress;

    private String id;
    private int isNewbie=0;

    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            id = params[0];
            DBserverAddress = params[1];

            String link = DBserverAddress+"checkNewMember.php?ID=" + id;
            Log.d("testAddress:",link);
            //URL url = new URL(link);
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(link));
            HttpResponse response = client.execute(request);
            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer sb = new StringBuffer("");
            String line = "";

            while ((line = in.readLine()) != null) {
                sb.append(line);
                break;
            }
            in.close();
            //return sb.toString();
            isNewbie = Integer.parseInt(sb.toString());

        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }


        //회원이 아닐 경우.
        if(isNewbie==-1) {

            for(int i=0; i<2; i++) {
                String phplist[] =
                        {
                                DBserverAddress+"signUp.php?ID=" + id +"&nick=BogBog",
                                DBserverAddress+"signUp2.php?nick=BogBog&point=10&sex=W&wheelchair=manual"
                        };
                try {
                    String link = phplist[i];
                    //URL url = new URL(link);
                    HttpClient client = new DefaultHttpClient();
                    HttpGet request = new HttpGet();
                    request.setURI(new URI(link));
                    HttpResponse response = client.execute(request);
                    BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    in.close();

                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }

        }

        return "";
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("ekgml ch:",result);

    }

    public String getID(){
        return id;
    }
}


