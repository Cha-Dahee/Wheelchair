package org.milal.wheeliric;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Yoojung on 2017-02-03.
 */

public class NewActivity extends AppCompatActivity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        //이전 액티비티로부터 정보를 넘겨받는다
        Bundle bundle = getIntent().getExtras();

        LatLng latLng = new LatLng(bundle.getDouble("latitude"), bundle.getDouble("longitude"));

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), latLng, bundle.getString("name"), bundle.getString("category"));
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabTextColors(Color.GRAY, (Color.parseColor("#e88091")));
        tabLayout.setSelectedTabIndicatorColor((Color.parseColor("#e88091")));
        tabLayout.setupWithViewPager(viewPager);
    }
}

class PagerAdapter extends FragmentPagerAdapter {

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
