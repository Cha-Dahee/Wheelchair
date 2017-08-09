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
 * Created by Yoojung on 2017-01-20.
 * 입력 받은 정보를 받아 TMap api를 사용하여 장소를 검색
 */

public class TMapPoiAPI extends AsyncTask<String, Void, List<Facility>>{

    private static final String APIKey = "95376795-5b4e-31aa-8445-869968fe3d20";
    private ProgressDialog progressDialog;
    Context mContext;
    List<TMapPOIItem> tList;
    List<Facility> mList;

    public TMapPoiAPI(Context context){
        mContext = context;
        progressDialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {

        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("로딩중입니다..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<Facility> result) {
        progressDialog.dismiss();
        super.onPostExecute(result);
    }

    @Override
    protected void onCancelled(List<Facility> result){
        if(progressDialog != null)
            progressDialog.dismiss();

        super.onCancelled();
    }

    @Override
    protected List<Facility> doInBackground(String... values) {

        TMapTapi tmaptapi = new TMapTapi(mContext);
        tmaptapi.setSKPMapAuthentication(APIKey);
        TMapData tmapdata = new TMapData();
        String address = values[0];

        tList = new ArrayList<>();
        mList = new ArrayList<>();

        try {
            tList = tmapdata.findAllPOI(address, 200);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        if(tList != null){
            StringBuilder mAddress = new StringBuilder();
            for (TMapPOIItem i : tList) {
                String name = i.getPOIName();
                TMapPoint point = i.getPOIPoint();
                double tLat = point.getLatitude();
                double tLng = point.getLongitude();
                String category = String.valueOf(i.middleBizName);

                if (i.upperAddrName != null)
                    mAddress.append(i.upperAddrName);
                if (i.middleAddrName != null)
                    mAddress.append(" ").append(i.middleAddrName);
                if (i.lowerAddrName != null)
                    mAddress.append(" ").append(i.lowerAddrName);
                if (i.detailAddrName != null)
                    mAddress.append(" ").append(i.detailAddrName);

                String vin = mAddress.toString();
                mList.add(new Facility(name, category, vin, tLat, tLng));
                mAddress.delete(0, mAddress.length());
            }
        }
        return mList;
    }
}
