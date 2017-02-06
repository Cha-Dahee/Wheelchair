package org.milal.wheeliric;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
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

    private TMapGeoAPI geo;
    private JsoupParser parser;
    private Facility facility;

    private ArrayList<String> list; //listview에 연결할 모델 객체
    private ArrayAdapter<String> adapter;



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
        list = new ArrayList<String>();
        //리스트뷰에 모델객체를 연결할 아답타 객체
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);

        //리스트뷰에 아답타 연결하기
        listView1.setAdapter(adapter);
        listView1.setDivider(new ColorDrawable(Color.RED));
        listView1.setDividerHeight(3); //구분선
        listView1.setOnItemClickListener(itemClickListenerOfSearchResult);


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

        list.clear();//새 액티비티마다 데이터 쌓임 방지(필요?)
        DaumCafeList thread = new DaumCafeList(facility.getName());
        thread.execute();
        try {
            for(int i=0; i < thread.get().size(); i++)
                this.list.add(thread.get().get(i));
            //this.list = thread.get(); 왜 위에는 되고 밑에는 안될까.
        }catch(Exception e){
            e.printStackTrace();
        }
        //모델의 데이터가 바뀌었다고 아답타 객체에 알린다.
        adapter.notifyDataSetChanged();

        textView1.setText(facility.getName());
        textView2.setText(facility.getVicinity());
        textView3.setText(facility.getInfo());

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

    private AdapterView.OnItemClickListener itemClickListenerOfSearchResult = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View clickedView, int position, long id) {
            String str = ((TextView)clickedView).getText().toString();
            String cafeLink = str.split("\n")[1];
            Intent CafePosting = new Intent(Intent.ACTION_VIEW, Uri.parse(cafeLink));
            // Intent CafePosting = new Intent(getApplicationContext(), ShowCafePosting.class);
            startActivity(CafePosting);
        }
    };

}
