package org.milal.wheeliric;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class ReviewFragment extends Fragment {

    private Facility facility;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view;

        ListView reviewListView;
        ReviewAdapter reviewAdapter;

        ArrayList<ReviewItems> reviewItemsArrayList;

        view = inflater.inflate(R.layout.fragment_review, null);

        reviewListView = (ListView) view.findViewById(R.id.review_list);

        reviewItemsArrayList = new ArrayList<ReviewItems>();

        reviewItemsArrayList.add(new ReviewItems
                (R.drawable.starbucks1,
                        "정하은",
                        "2017-08-01",
                        R.drawable.toilet,
                        R.drawable.entrance,
                        R.drawable.parking,
                        R.drawable.table,
                        "★★★★★", "맛있어요",R.drawable.heart_filled, 10));

        reviewItemsArrayList.add(new ReviewItems
                (R.drawable.starbucks2,
                        "신유정",
                        "2017-06-17",
                        R.drawable.toilet,
                        R.drawable.entrance,
                        R.drawable.parking,
                        R.drawable.table,
                        "★★★☆☆", "문턱 때문에 전동 휠체어가 지나갈수는 없겠네요",R.drawable.heart_empty, 5));

        //여기 다시 체크해보기.
        reviewAdapter = new ReviewAdapter(this.getActivity(), reviewItemsArrayList);
        reviewListView.setAdapter(reviewAdapter);



        Button writeReviewBtn = (Button) view.findViewById(R.id.write_review);
        /*
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "리뷰를 쓸거얌", Toast.LENGTH_SHORT).show();
            }
        });
        */
        writeReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WriteReview.class);
                startActivity(intent);
            }
        });
        facility = new Facility();
        facility.setName(getArguments().getString("name"));
        facility.setCategory(getArguments().getString("category"));
        facility.setLat(getArguments().getDouble("latitude"));
        facility.setLng(getArguments().getDouble("longitude"));

       // Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.us);
       // facility.setImage(bitmap);

        //textView1.setText(facility.getName());
        //textView2.setText(facility.getVicinity());

       // image1.setImageBitmap(facility.getImage().get(0));


        return view;
    }

    public static ReviewFragment newInstance(LatLng latLng, String name, String category) {
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putDouble("latitude", latLng.latitude);
        args.putDouble("longitude", latLng.longitude);
        args.putString("category", category);
        ReviewFragment fragment = new ReviewFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
