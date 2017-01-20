package org.milal.wheeliric;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

/**
 * Created by Yoojung on 2017-01-17.
 * 마커 클릭시 생성되는 새로운 액티비티
 * 마커에 대한 정보(이름과 주소)를 받아온다
 */

public class NewActivity extends AppCompatActivity {

    private String name;
    private String attribution;
    private double latitude;
    private double longitude;

    private TextView textView;
    private TextView textView2;

    private TMapGeoAPI geo;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);

        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        latitude = bundle.getDouble("latitude");
        longitude = bundle.getDouble("longitude");

        geo = new TMapGeoAPI(this);
        try {
            attribution = geo.execute(latitude, longitude).get();
            textView.setText(name);
            textView2.setText(attribution);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
