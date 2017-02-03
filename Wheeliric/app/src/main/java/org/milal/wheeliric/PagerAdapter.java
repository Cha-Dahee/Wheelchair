package org.milal.wheeliric;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Yoojung on 2017-02-03.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    private LatLng latLng;
    private String name;
    private String category;

    private static int PAGE_NUMBER = 2;

    public PagerAdapter(FragmentManager fm, LatLng latLng, String name, String category){
        super(fm);
        this.latLng = latLng;
        this.name = name;
        this.category = category;
    }
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return InfoFragment.newInstance(latLng, name, category);
            case 1:
                return ReviewFragment.newInstance(latLng, name, category);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return PAGE_NUMBER;
    }

    @Override
    public CharSequence getPageTitle(int position){
        switch(position){
            case 0:
                return "정보";
            case 1:
                return "리뷰";
            default:
                return null;
        }
    }
}
