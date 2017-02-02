package org.milal.wheeliric;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

/**
 * Created by Yoojung on 2017-01-17.
 * 마커 클릭시 생성되는 새로운 액티비티
 * 마커에 대한 정보(이름, 위치, 카테고리)를 받아온다
 */

public class NewActivity extends AppCompatActivity {

    private static final int NEW_ADDRESS = 0;
    private static final int OLD_ADDRESS = 1;

    final static String DAEGU_FOOD = "DAEGU_FOOD";
    final static String DAEGU_STAY = "DAEGU_STAY";
    final static String SEOUL = "SEOUL";
    final static String ULSAN = "ULSAN";
    final static String GYEONGGI = "GYEONGGI";
    final static String TOUR = "TOUR";

    private TextView textView;
    private TextView textView2;
    private TextView textView3;

    private TMapGeoAPI geo;
    private JsoupParser parser;
    Facility facility;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        ImageView image1 = (ImageView) findViewById(R.id.image1);
        ImageView image2 = (ImageView) findViewById(R.id.image2);
        ImageView image3 = (ImageView) findViewById(R.id.image3);

        //정보를 넘겨받는다
        Bundle bundle = getIntent().getExtras();
        facility = new Facility();
        facility.setName(bundle.getString("name"));
        facility.setLat(bundle.getDouble("latitude"));
        facility.setLng(bundle.getDouble("longitude"));
        facility.setCategory(bundle.getString("category"));

        geo = new TMapGeoAPI(this);
        parser = new JsoupParser();
        try {
            Facility info;

            //TMapGeoAPI로 부터 주소를 받아옴(옛주소, 신주소 선택 가능)
            facility.setVicinity((geo.execute(facility.getLat(), facility.getLng()).get())[NEW_ADDRESS]);

            //주소에 따라 검색하는 사이트 상이
            if(facility.getVicinity().contains("대구") && facility.getCategory().contains("숙박")){
                info = parser.execute(DAEGU_STAY, facility.getName()).get();
            } else if(facility.getVicinity().contains("대구")){
                info = parser.execute(DAEGU_FOOD, facility.getName()).get();
            } else if(facility.getVicinity().contains("서울")){
                info = parser.execute(SEOUL, facility.getName()).get();
            } /*else if(facility.getVicinity().contains("울산")){
                info = parser.execute(ULSAN, facility.getName()).get();
            } else if(facility.getVicinity().contains("경기")){
                info = parser.execute(GYEONGGI, facility.getName()).get();
            } */else{
                info = parser.execute(TOUR, facility.getName()).get();
            }

            facility.setInfo(info.getInfo());
            facility.setImage(info.getImage());

            //검색 결과가 존재하며 이미지 또한 존재할 때
            if(!facility.getInfo().equals("검색결과가 없습니다.") && !facility.getImage().isEmpty()) {
                image1.setImageBitmap(facility.getImage().get(0));
                image2.setImageBitmap(facility.getImage().get(1));
                image3.setImageBitmap(facility.getImage().get(2));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        textView.setText(facility.getName());
        textView2.setText(facility.getVicinity());
        textView3.setText(facility.getInfo());
    }
}
