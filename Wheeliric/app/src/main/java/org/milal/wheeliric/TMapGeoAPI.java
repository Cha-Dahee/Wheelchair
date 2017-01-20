package org.milal.wheeliric;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.skp.Tmap.TMapAddressInfo;
import com.skp.Tmap.TMapData;

import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Yoojung on 2017-01-19.
 * TMap Api를 사용한 역지오코딩
 * Google Api 보다 정확한듯..
 */

public class TMapGeoAPI extends AsyncTask<Double, Void, String>{

    private Context mContext;
    private TMapData tmapdata;
    private TMapAddressInfo addressInfo;
    private ProgressDialog asyncDialog;
    String address;

    public TMapGeoAPI(Context context){
        mContext = context;
    }

    @Override
    protected void onPreExecute(){
        asyncDialog = new ProgressDialog(mContext);
        asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        asyncDialog.setMessage("로딩중입니다..");
        asyncDialog.show();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Double... values){

        double latitude = values[0];
        double longitude = values[1];

        tmapdata = new TMapData();
        addressInfo = new TMapAddressInfo();

        try {
            addressInfo = tmapdata.reverseGeocoding(latitude, longitude, "A02");
            address = addressInfo.strFullAddress;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        return address;
    }

    @Override
    protected void onPostExecute(String result){
        asyncDialog.dismiss();
    }
}
