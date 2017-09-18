package org.milal.wheeliric;

import android.util.Log;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Dahee Joy Cha on 2017-09-18.
 */

public class AccountFunction {

    URL url;

    public AccountFunction(String url1){
        this.url = new URL(ur/l1);

    }
    //새로운 회원인지 확인합니다.
    public String newMemberCheck(final String data_id) {
        try {
            String postData = "Data1=" +data_id;
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(postData.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
            String result = readStream(conn.getInputStream());
            conn.disconnect();
            return result;
        }
        catch (Exception e) {
            Log.i("PHPRequest", "request was failed.");
            return null;
        }
    }

}
