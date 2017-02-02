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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GpsInfo gps;
    private TMapAPI places;
    private GoogleGeoAPI geo;

    List<Facility> mList = null;
    //List<Marker> pMarker = null;

    private double latitude;
    private double longitude;
    private String categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Bundle bundle = getIntent().getExtras();
        categories = bundle.getString("categories");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        String vicinity;
        //pMarker = new ArrayList<>();
        mMap = googleMap;
        geo = new GoogleGeoAPI();

        //현재위치를 받아 온다
        gps = new GpsInfo(MapsActivity.this);
        if(gps.isGetLocation()){
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        } else{
            gps.showSettingAlert();
        }

        //받아온 현재 위치를 마커로 만든다
        LatLng myPosition = new LatLng(latitude, longitude);
        try {
            vicinity = geo.execute(myPosition).get();
            mMap.addMarker(new MarkerOptions().position(myPosition).title("현재위치").snippet(vicinity)).showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myPosition));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            addMarker(mMap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    //API를 사용해 받아온 장소 정보를 마커로 만든다
    public void addMarker(GoogleMap googleMap) throws ExecutionException, InterruptedException {

        /*GooglePlaceAPI places = new GooglePlaceAPI();
        mList = new ArrayList<>();
        mList = places.execute(36.082141, 129.398496, 500.0).get();*/

        places = new TMapAPI(MapsActivity.this, categories);
        mList = new ArrayList<>();
        mList = places.execute(latitude, longitude).get();

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
            }
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

        /*HashSet<Marker> hashSet = new HashSet<Marker>();
        hashSet.addAll(pMarker);
        pMarker.clear();
        pMarker.addAll(hashSet);*/
    }
}
