package org.milal.wheeliric;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yoojung on 2017-01-17.
 * 마커에 담을 장소를 만들기 위한 클래스
 */

public class Facility {
    private String name;
    private String category;
    private String vicinity;
    private String info;
    private double lat;
    private double lng;
    private LatLng latLng;
    private List<Bitmap> image;


    public Facility(){
        info = "";
        image = new ArrayList<>();
    }

    public Facility(String name, String category, String vicinity, double lat, double lng){
        this.name = name;
        this.category = category;
        this.vicinity = vicinity;
        this.lat = lat;
        this.lng = lng;
        latLng = new LatLng(lat, lng);
    }

    public void setName(String name){this.name = name;}
    public void setCategory(String category){this.category = category;}
    public void setVicinity(String vicinity){this.vicinity = vicinity;}
    public void setLat(double lat){this.lat = lat;}
    public void setLng(double lng){this.lng = lng;}
    public void setLatLng(double lat, double lng){this.lat = lat; this.lng = lng;}
    public void setInfo(String info){this.info = info;}
    public void setImage(Bitmap image){this.image.add(image);}
    public void setImage(List<Bitmap> image){this.image = image;}

    public String getInfo(){return info;}
    public List<Bitmap> getImage(){return image;}
    public String getName(){return name;}
    public String getCategory(){return category;}
    public String getVicinity(){return vicinity;}
    public double getLat(){return lat;}
    public double getLng(){return lng;}
    public LatLng getLatLng(){return latLng;}


}
