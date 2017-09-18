package org.milal.wheeliric;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import java.net.URL;
import java.net.HttpURLConnection;
import java.util.HashMap;

import static android.R.attr.name;
import static com.google.android.gms.wearable.DataMap.TAG;
import static java.sql.Types.NULL;
import static org.milal.wheeliric.R.drawable.entrance;

public class ReviewFragment extends Fragment {

    private Facility facility;


    private static final String TAG_JSON="review";
    private static final String TAG_review_idx = "review_idx";
    private static final String TAG_facility_idx = "facility_idx";
    private static final String TAG_date ="date";
    private static final String TAG_writer = "writer";
    private static final String TAG_access = "access";
    private static final String TAG_likeN ="likeN";
    private static final String TAG_toilet = "toilet";
    private static final String TAG_park = "park";
    private static final String TAG_elev ="elev";
    //mysql 고유어 table과 겹쳐서 table의 유무는 tabl로 나타냄.
    private static final String TAG_tabl ="tabl";
    private static final String TAG_grade ="grade";
    private static final String TAG_comment ="comment";

    String mJsonString;

    ArrayList<ReviewItems> reviewItemsArrayList;
    ReviewAdapter reviewAdapter;
    ListView reviewListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view;
        
        view = inflater.inflate(R.layout.fragment_review, null);

        reviewListView = (ListView) view.findViewById(R.id.review_list);

        GetData task = new GetData();
        task.execute("http://ec2-13-124-46-181.ap-northeast-2.compute.amazonaws.com/getReview.php");

        Button writeReviewBtn = (Button) view.findViewById(R.id.write_review);
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

        return view;
    }

    public class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //여기!
            progressDialog = ProgressDialog.show(getActivity(),
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            progressDialog.dismiss();
            //mTextViewResult.setText(result);
            Log.d(TAG, "response is... - " + result);

            if (result == null){

            //    mTextViewResult.setText(errorString);
            }
            else {

                mJsonString = result;
                showResult();
                //여기 다시 체크해보기.
                reviewAdapter = new ReviewAdapter(getActivity(), reviewItemsArrayList);
                reviewListView.setAdapter(reviewAdapter);
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }
        }

        private void showResult(){

            try {
                reviewItemsArrayList = new ArrayList<ReviewItems>();

                JSONObject jsonObject = new JSONObject(mJsonString);

                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for(int i=0;i<jsonArray.length();i++){

                    JSONObject item = jsonArray.getJSONObject(i);

                    //없는건 나중에 넣자.
                    int review_idx = Integer.parseInt(item.getString(TAG_review_idx));
                    int facility_idx = Integer.parseInt(item.getString(TAG_facility_idx));
                    String date = item.getString(TAG_date);
                    String writer = item.getString(TAG_writer);
                    String access = item.getString(TAG_access);
                    int accessResult=0;
                    int likeN = Integer.parseInt(item.getString(TAG_likeN));
                    int toilet = Integer.parseInt(item.getString(TAG_toilet));
                    int park = Integer.parseInt(item.getString(TAG_park));
                    int elev = Integer.parseInt(item.getString(TAG_elev));
                    int tabl = Integer.parseInt(item.getString(TAG_tabl));
                    int grade = Integer.parseInt(item.getString(TAG_grade));
                    String gradeResult=null;
                    String comment = item.getString(TAG_comment);

                    switch(grade) {
                        case 0:
                            gradeResult="☆☆☆☆☆";
                            break;
                        case 1:
                            gradeResult="★☆☆☆☆";
                            break;
                        case 2:
                            gradeResult="★★☆☆☆";
                            break;
                        case 3:
                            gradeResult="★★★☆☆";
                            break;
                        case 4:
                            gradeResult="★★★★☆";
                            break;
                        case 5:
                            gradeResult="★★★★★";
                            break;
                    }

                    //toilet 유무
                    if(toilet==0)
                        toilet=NULL;
                    else
                        toilet= R.drawable.toilet;

                    //접근가능성-가능, 문턱(th), 계단(st)
                    if(access.equals('Y'))
                        accessResult=R.drawable.entrance;
                    else if(access.equals('t'))
                        accessResult=NULL;
                    else
                        accessResult=NULL;

                    //주차공간 유무 - 사진 바꾸기..
                    if(park==0)
                        park=NULL;
                    else
                        park= R.drawable.parking;

                    //elev
                    if(elev==0)
                        elev=NULL;
                    else
                        elev= NULL;

                    //table 유무확인
                    if(tabl==0)
                        tabl=NULL;
                    else
                        tabl= R.drawable.table;



                    reviewItemsArrayList.add(new ReviewItems
                            (R.drawable.starbucks1,
                                    writer,
                                    date,
                                    toilet,
                                    accessResult,
                                    park,
                                    tabl,
                                    gradeResult, comment,R.drawable.heart_filled, likeN));
                }

                reviewItemsArrayList.add(new ReviewItems
                        (R.drawable.starbucks2,
                                "신유정",
                                "2017-06-17",
                                R.drawable.toilet,
                                entrance,
                                R.drawable.parking,
                                R.drawable.table,
                                "★★★☆☆", "문턱 때문에 전동 휠체어가 지나갈수는 없겠네요",R.drawable.heart_empty, 5));


            } catch (JSONException e) {
            }
        }
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
