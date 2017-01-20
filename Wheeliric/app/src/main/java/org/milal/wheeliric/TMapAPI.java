package org.milal.wheeliric;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapTapi;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Yoojung on 2017-01-18.
 * 위치를 이용해 반경안에 있는 장소를 찾아주는 TMap API
 * Categories와 radius를 변경 할 수 있다.
 */

public class TMapAPI extends AsyncTask<Double, Void, List<Facility>> {

    Context mContext;
    private double latitude;
    private double longitude;
    private String categories;
    private int radius;
    private static final String APIKey = "95376795-5b4e-31aa-8445-869968fe3d20";

    List<TMapPOIItem> tList = new ArrayList<>();
    List<Facility> mList = new ArrayList<>();
    ProgressDialog asyncDialog;

    public TMapAPI(Context context, String categories){
        this.mContext = context;
        this.categories = categories;
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
    protected List<Facility> doInBackground(Double... values) {

        TMapTapi tmaptapi = new TMapTapi(mContext);
        tmaptapi.setSKPMapAuthentication(APIKey);
        TMapData tmapdata = new TMapData();

        latitude = values[0];
        longitude = values[1];
        radius = 10;

        TMapPoint tpoint = new TMapPoint(latitude, longitude);
        try {
            tList = tmapdata.findAroundNamePOI(tpoint, categories, radius, 200);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        StringBuilder mAddress = new StringBuilder();
        for (TMapPOIItem i : tList) {
            String address;
            String id = i.getPOIID();
            String name = i.getPOIName();
            TMapPoint point = i.getPOIPoint();
            double tLat = point.getLatitude();
            double tLng = point.getLongitude();

            if(i.upperAddrName != null)
                mAddress.append(i.upperAddrName);
            if(i.middleAddrName != null)
                mAddress.append(" ").append(i.middleAddrName);
            if(i.lowerAddrName != null)
                mAddress.append(" ").append(i.lowerAddrName);
            if(i.detailAddrName != null)
                mAddress.append(" ").append(i.detailAddrName);

            String vin = mAddress.toString();
            mList.add(new Facility(name, id, vin, tLat, tLng));
            mAddress.delete(0, mAddress.length());
        }
        return mList;
    }

    @Override
    protected void onPostExecute(List<Facility> result){
        asyncDialog.dismiss();
    }
}
