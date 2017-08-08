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

public class TMapGeoAPI extends AsyncTask<Double, Void, String[]>{

    private static final int NEW_ADDRESS = 0;
    private static final int OLD_ADDRESS = 1;
    private Context mContext;
    private TMapData tmapdata;
    private TMapAddressInfo addressInfo;
    private ProgressDialog progressDialog;
    String address[];

    public TMapGeoAPI(Context context){
        mContext = context;
        progressDialog = new ProgressDialog(mContext);
    }

    @Override
    protected void onPreExecute() {

        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("로딩중입니다..");
        progressDialog.show();

        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String[] result) {
        progressDialog.dismiss();

        super.onPostExecute(result);
    }

    @Override
    protected String[] doInBackground(Double... values){

        double latitude = values[0];
        double longitude = values[1];

        tmapdata = new TMapData();
        addressInfo = new TMapAddressInfo();
        address = new String[2];

        try {
            addressInfo = tmapdata.reverseGeocoding(latitude, longitude, "A03");
            address[NEW_ADDRESS] = addressInfo.strFullAddress;

            addressInfo = tmapdata.reverseGeocoding(latitude, longitude, "A02");
            address[OLD_ADDRESS] = addressInfo.strFullAddress;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        return address;
    }
}
