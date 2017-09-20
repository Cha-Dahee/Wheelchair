package org.milal.wheeliric;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.milal.wheeliric.Main2Activity.ecch;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TMapPoiAPI places;
    List<Facility> mList = null;
    String address = "";
    Boolean m = true; //검색값 확인

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //직접 검색한 내용을 받아온다
        Bundle bundle = getIntent().getExtras();
        address = bundle.getString("address");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            addMarker(mMap);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //TMap api에서 직접 검색하여 받아온 장소 정보를 마커로 가져온다
    public void addMarker(GoogleMap googleMap) throws ExecutionException, InterruptedException {

        places = new TMapPoiAPI(MapsActivity2.this);
        mList = new ArrayList<>();
        mList = places.execute(address).get();

        if(mList.isEmpty()){
            Toast.makeText(getApplicationContext(), "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            for (Facility i : mList) {
                LatLng latLng = new LatLng(i.getLat(), i.getLng());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(i.getName());

                if(!i.getCategory().equals("null")){
                    markerOptions.snippet(i.getVicinity() + "(" + i.getCategory() + ")");
                } else{
                    markerOptions.snippet(i.getVicinity());
                }
                markerOptions.alpha(0.6f);
                // Marker item =
                googleMap.addMarker(markerOptions);
                //pMarker.add(item);

                //입력값과 정확히 매칭되는 상호명이 있을 경우 stop
                if(ecch) {
                    String[] arr = address.split(" ");
                    if(i.getName().equals(arr[2])) {
                        m = false;
                        break;
                    }
                }
            }

            if(ecch && m==true ) {
                Toast.makeText(getApplicationContext(), " 정확히 일치하는 검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }

            LatLng latLng = mList.get(0).getLatLng();
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }

        //말풍선 클릭 리쓰너
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener(){

            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(getApplicationContext(), NewActivity.class);
                intent.putExtra("name", marker.getTitle());
                intent.putExtra("longitude", marker.getPosition().longitude);
                intent.putExtra("latitude", marker.getPosition().latitude);

                if(marker.getSnippet().contains("(")){
                    int i = marker.getSnippet().indexOf("(");
                    String s = marker.getSnippet().substring(i+1, (marker.getSnippet().length())-1);
                    intent.putExtra("category", s);
                } else{
                    intent.putExtra("category", "null");
                }

                startActivity(intent);
            }
        });

    }
}