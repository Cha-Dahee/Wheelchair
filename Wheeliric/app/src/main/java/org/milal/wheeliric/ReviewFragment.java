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

import static org.milal.wheeliric.R.id.write;

public class ReviewFragment extends Fragment {

    private Facility facility;

    public ReviewFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_review, container, false);

        TextView textView1 = (TextView) view.findViewById(R.id.textView1);
        TextView textView2 = (TextView) view.findViewById(R.id.textView2);

        ImageView image1 = (ImageView) view.findViewById(R.id.image1);

        Button button = (Button) view.findViewById(write);

        facility = new Facility();
        facility.setName(getArguments().getString("name"));
        facility.setCategory(getArguments().getString("category"));
        facility.setLat(getArguments().getDouble("latitude"));
        facility.setLng(getArguments().getDouble("longitude"));

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.us);
        facility.setImage(bitmap);

        textView1.setText(facility.getName());
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "리뷰를 쓸거얌", Toast.LENGTH_SHORT).show();
            }
        });

        image1.setImageBitmap(facility.getImage().get(0));
        textView2.setText("★★★★☆    우와 짱짱이에여~~ 너무 좋아여~~");

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
