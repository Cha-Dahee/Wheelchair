package org.milal.wheeliric;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.concurrent.ExecutionException;

public class InfoFragment extends Fragment {

    private static final int NEW_ADDRESS = 0;
    private static final int OLD_ADDRESS = 1;

    final static String DAEGU_FOOD = "DAEGU_FOOD";
    final static String DAEGU_STAY = "DAEGU_STAY";
    final static String SEOUL = "SEOUL";
    final static String ULSAN = "ULSAN";
    final static String GYEONGGI = "GYEONGGI";
    final static String TOUR = "TOUR";
    final static String GWANGJOO = "GWANGJOO";
    final static String GWANGJOO_FOOD = "GWANGJOO_FOOD";

    private TMapGeoAPI geo;
    private JsoupParser parser;
    private Facility facility;

    public InfoFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_info, container, false);

        ImageView image1 = (ImageView) view.findViewById(R.id.image1);
        ImageView image2 = (ImageView) view.findViewById(R.id.image2);
        ImageView image3 = (ImageView) view.findViewById(R.id.image3);

        //TextView - 1:이름; 2:주소; 3:정보
        TextView textView1 = (TextView) view.findViewById(R.id.textView1);
        TextView textView2 = (TextView) view.findViewById(R.id.textView2);
        TextView textView3 = (TextView) view.findViewById(R.id.textView3);

        //네이버, 다음 카페 및 블로그 리스트 정보
        ListView listView1 = (ListView) view.findViewById(R.id.listView1);

        geo = new TMapGeoAPI(view.getContext());
        parser = new JsoupParser();
        facility = new Facility();
        facility.setName(getArguments().getString("name"));
        facility.setCategory(getArguments().getString("category"));
        facility.setLat(getArguments().getDouble("latitude"));
        facility.setLng(getArguments().getDouble("longitude"));

        try {
            Facility info;

            //TMapGeoAPI로 부터 주소를 받아옴(옛주소, 신주소 선택 가능)
            facility.setVicinity((geo.execute(facility.getLat(), facility.getLng()).get())[NEW_ADDRESS]);

            Boolean flag = false;

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
            } */ else{
                info = parser.execute(TOUR, facility.getName()).get();
                flag = true;
            }

            if(flag == false && info.getInfo().equals("검색결과가 없습니다.")){
                JsoupParser parser2 = new JsoupParser();
                info = parser2.execute(TOUR, facility.getName()).get();
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

        textView1.setText(facility.getName());
        textView2.setText(facility.getVicinity());
        textView3.setText(facility.getInfo());
        //listView1.setAdapter(...);

        return view;
    }

    public static InfoFragment newInstance(LatLng latLng, String name, String category){
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putDouble("latitude", latLng.latitude);
        args.putDouble("longitude", latLng.longitude);
        args.putString("category", category);
        InfoFragment fragment = new InfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
