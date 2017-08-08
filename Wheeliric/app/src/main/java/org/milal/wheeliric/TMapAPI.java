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
    private String categories;
    private ProgressDialog progressDialog;
    private static final String APIKey = "95376795-5b4e-31aa-8445-869968fe3d20";

    List<TMapPOIItem> tList;
    List<Facility> mList;

    public TMapAPI(Context context, String categories){
        this.mContext = context;
        this.categories = categories;
        progressDialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {

        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("로딩중입니다..");
        progressDialog.show();

        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<Facility> result) {
        progressDialog.dismiss();
        super.onPostExecute(result);
    }

    @Override
    protected List<Facility> doInBackground(Double... values) {

        TMapTapi tmaptapi = new TMapTapi(mContext);
        tmaptapi.setSKPMapAuthentication(APIKey);
        TMapData tmapdata = new TMapData();

        double latitude = values[0];
        double longitude = values[1];
        int radius = 10;

        tList = new ArrayList<>();
        mList = new ArrayList<>();

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

        if(tList != null){
            StringBuilder mAddress = new StringBuilder();
            for (TMapPOIItem i : tList) {
                String name = i.getPOIName();
                TMapPoint point = i.getPOIPoint();
                double tLat = point.getLatitude();
                double tLng = point.getLongitude();

                String category = "null";
                if(categories.equals("숙박"))
                    category = "숙박";

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
