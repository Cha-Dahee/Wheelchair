package org.milal.wheeliric;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
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
        tabLayout.setupWithViewPager(viewPager);
    }
}
