package org.milal.wheeliric;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class ReviewFragment extends Fragment {

    private Facility facility;

    private String[] reviewItems = {"2017년 8월 1일\n정하은 님", "2017년 8월 6일\n신유정 님"};

    public ReviewFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_review, null);

        TextView textView1 = (TextView) view.findViewById(R.id.textView1);
        TextView textView2 = (TextView) view.findViewById(R.id.textView2);

        //ArrayAdapter reviewAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, reviewItems) ;

        //ListView reviewList = (ListView) view.findViewById(R.id.reviews);

        //ArrayList<ReviewItems> data = new ArrayList<>();
        /*
        for(int i = 0; i<reviewItems.length;i++){
            ReviewItems review = new ReviewItems(R.drawable.us,"정하은","Y","Y","Y","Y",10,"Y");
            data.add(review);
        }


        ReviewAdapter adapter=new ReviewAdapter(this,R.layout.review_items, data);
        reviewList.setAdapter(adapter);
        */

        ImageView image1 = (ImageView) view.findViewById(R.id.image1);

        Button button = (Button) view.findViewById(R.id.write);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "리뷰를 쓸거얌", Toast.LENGTH_SHORT).show();
            }
        });

        facility = new Facility();
        facility.setName(getArguments().getString("name"));
        facility.setCategory(getArguments().getString("category"));
        facility.setLat(getArguments().getDouble("latitude"));
        facility.setLng(getArguments().getDouble("longitude"));

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.us);
        facility.setImage(bitmap);

        textView1.setText(facility.getName());
        textView2.setText("★★★★☆    우와 짱짱이에여~~ 너무 좋아여~~");

        image1.setImageBitmap(facility.getImage().get(0));


        return view;
    }

    public static ReviewFragment newInstance(LatLng latLng, String name, String category){
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
