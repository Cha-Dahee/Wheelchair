package org.milal.wheeliric;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    final static String GWANGJOO = "GWANGJOO";
    final static String GWANGJOO_FOOD = "GWANGJOO_FOOD";

    private TMapGeoAPI geo;
    //private JsoupParser Jparser;
    private HTMLParser Hparser;
    private Facility facility;

    private String[] images;
    private String[] urls;

    private GridView gridView;
    private GridView gridView2;
    private Tensorflow tensorflow;
    private Tensorflow tensorflow2;
    private DaumCafeList daumCafeList;
    private NaverBlogList naverBlogList;

    public InfoFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_info, container, false);
        final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linear);
        gridView = (GridView) view.findViewById(R.id.gridView);
        gridView2 = (GridView) view.findViewById(R.id.gridView2);
        final Button button = (Button) view.findViewById(R.id.more);

        ImageView image1 = (ImageView) view.findViewById(R.id.image1);
        ImageView image2 = (ImageView) view.findViewById(R.id.image2);
        ImageView image3 = (ImageView) view.findViewById(R.id.image3);

        //TextView 1:이름; 2:주소; 3:정보; 4:image 검색 url어레이 위치
        TextView textView1 = (TextView) view.findViewById(R.id.textView1);
        TextView textView2 = (TextView) view.findViewById(R.id.textView2);
        TextView textView3 = (TextView) view.findViewById(R.id.textView3);
        final TextView hidden = (TextView) view.findViewById(R.id.hidden);

        //다음 카페 리스트 정보
        ListView listView = (ListView) view.findViewById(R.id.listView1);
        TextView textView4 = (TextView) view.findViewById(R.id.textView4);

        //네이버 카페 및 블로그 리스트 정보
        ListView listView2 = (ListView) view.findViewById(R.id.listView2);
        TextView textView5 = (TextView) view.findViewById(R.id.textView5);

        Hparser = new HTMLParser(getActivity());
        images = new String[100];
        urls = new String[100];

        facility = new Facility();
        facility.setName(getArguments().getString("name"));
        facility.setCategory(getArguments().getString("category"));
        facility.setLat(getArguments().getDouble("latitude"));
        facility.setLng(getArguments().getDouble("longitude"));

        geo = new TMapGeoAPI(getActivity());
        try {
            facility.setVicinity(geo.execute(facility.getLat(), facility.getLng()).get()[OLD_ADDRESS]);
            facility.setInfo("검색결과가 없습니다.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        textView1.setText(facility.getName());
        textView2.setText(facility.getVicinity());
        textView3.setText(facility.getInfo());

        String[] address = facility.getVicinity().split(" ");

        try {
            ArrayList<URLObject> list = Hparser.execute(address[1] + " " + facility.getName()).get();
            for (int i = 0; i < list.size() && i < 100; i++) {
                images[i] = list.get(i).getImage();
                urls[i] = list.get(i).getUrl();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        tensorflow = new Tensorflow(getActivity(), gridView, hidden);
        tensorflow.execute(images, urls);

        daumCafeList = new DaumCafeList(getActivity(), listView, textView4, address[1] + " " + facility.getName());
        daumCafeList.execute();

        naverBlogList = new NaverBlogList(getActivity(), listView2, textView5);
        naverBlogList.execute(address[1] + " " + facility.getName());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tensorflow2 = new Tensorflow(getActivity(), gridView2, hidden);
                tensorflow2.execute(images, urls);
                linearLayout.removeView(button);
                linearLayout.removeView(hidden);
            }
        });

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

    //공공기관
    public void JsoupParser(){
        /*
        Jparser = new JsoupParser();
        try {
            Facility info;

            //TMapGeoAPI로 부터 주소를 받아옴(옛주소, 신주소 선택 가능)
            facility.setVicinity((geo.execute(facility.getLat(), facility.getLng()).get())[NEW_ADDRESS]);
            facility.setInfo("검색결과가 없습니다.");

            Boolean flag = false;


            //주소에 따라 검색하는 사이트 상이
            if(facility.getVicinity().contains("대구") && facility.getCategory().contains("숙박")){
                info = Jparser.execute(DAEGU_STAY, facility.getName()).get();
            } else if(facility.getVicinity().contains("대구")){
                info = Jparser.execute(DAEGU_FOOD, facility.getName()).get();
            } else if(facility.getVicinity().contains("서울")){
                info = Jparser.execute(SEOUL, facility.getName()).get();
            } /*else if(facility.getVicinity().contains("울산")){
                info = parser.execute(ULSAN, facility.getName()).get();
            } else if(facility.getVicinity().contains("경기")){
                info = parser.execute(GYEONGGI, facility.getName()).get();
            } *//* else if(facility.getVicinity().contains("광주")){
                info = Jparser.execute(GWANGJOO_FOOD, facility.getName()).get();

                /*if(info.getInfo().equals("검색결과가 없습니다.")){
                    JsoupParser parser2 = new JsoupParser();
                    info = parser2.execute(GWANGJOO_FOOD, facility.getName()).get();
                }*//*

            } else {
                info = Jparser.execute(TOUR, facility.getName()).get();
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
        }*/
    }
}
