package org.milal.wheeliric;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

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

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TMapPoiAPI places;
    List<Facility> mList = null;
    String address = "";

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

        for (Facility i : mList) {
            LatLng latLng = new LatLng(i.getLat(), i.getLng());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(i.getName());
            markerOptions.snippet(i.getVicinity());
            markerOptions.alpha(0.6f);
            // Marker item =
            googleMap.addMarker(markerOptions);
            //pMarker.add(item);
        }

        LatLng latLng = mList.get(0).getLatLng();
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        //말풍선 클릭 리쓰너
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener(){

            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(getApplicationContext(), NewActivity.class);
                intent.putExtra("name", marker.getTitle());
                intent.putExtra("longitude", marker.getPosition().longitude);
                intent.putExtra("latitude", marker.getPosition().latitude);
                startActivity(intent);
            }
        });

        /*HashSet<Marker> hashSet = new HashSet<Marker>();
        hashSet.addAll(pMarker);
        pMarker.clear();
        pMarker.addAll(hashSet);*/
    }
}