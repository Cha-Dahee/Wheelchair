package org.milal.wheeliric;

/**
 * Created by Yoojung on 2017-01-17.
 * 마커에 담을 장소를 만들기 위한 클래스
 */

public class Facility {
    String name;
    String id;
    String vicinity;
    double lat;
    double lng;

    public Facility(String name, String id, String vicinity, double lat, double lng){
        this.name = name;
        this.id = id;
        this.vicinity = vicinity;
        this.lat = lat;
        this.lng = lng;
    }

    void setName(String name){this.name = name;}
    void setId(String id){this.id = id;}
    void setVicinity(String vicinity){this.vicinity = vicinity;}
    void setLat(double lat){this.lat = lat;}
    void setLng(double lng){this.lng = lng;}

    String getName(){return name;}
    String getId(){return id;}
    String getVicinity(){return vicinity;}
    double getLat(){return lat;}
    double getLng(){return lng;}
}
