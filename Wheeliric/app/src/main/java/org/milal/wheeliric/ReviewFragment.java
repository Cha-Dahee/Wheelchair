package org.milal.wheeliric;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import static com.google.android.gms.wearable.DataMap.TAG;
import static java.sql.Types.NULL;
import static org.milal.wheeliric.R.string.DBserverAddress;

public class ReviewFragment extends Fragment {

    private Facility facility;


    private static final String TAG_JSON = "review";
    private static final String TAG_review_idx = "review_idx";
    private static final String TAG_facility_idx = "facility_idx";
    private static final String TAG_date = "date";
    private static final String TAG_writer = "writer";
    private static final String TAG_access = "access";
    private static final String TAG_likeN = "likeN";
    private static final String TAG_toilet = "toilet";
    private static final String TAG_park = "park";
    private static final String TAG_elev = "elev";
    //mysql 고유어 table과 겹쳐서 table의 유무는 tabl로 나타냄.
    private static final String TAG_tabl = "tabl";
    private static final String TAG_grade = "grade";
    private static final String TAG_comment = "comment";

    String reviewList;

    ArrayList<ReviewItems> reviewItemsArrayList;
    ReviewAdapter reviewAdapter;
    ListView reviewListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view;

        view = inflater.inflate(R.layout.fragment_review, null);

        reviewListView = (ListView) view.findViewById(R.id.review_list);

        //리뷰 데이터 얻어오기
        GetData task = new GetData();
        task.execute(getString(DBserverAddress) + "getReview.php");

        //리뷰 작성 페이지로 넘어가기
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

    public void checkHit() {
        //만약 한번도 방문된 적 없으면
        if (getDB(getString(DBserverAddress) + "isVisited.php") == null) {
            //FACILITY_INFO 테이블에 정보 넣기
            insertData(getString(DBserverAddress) + "addSearch.php");
            }
         else {//만약 방문한 적 있으면
            //FACILITY_INFO의 hit수 update. //이거 맞나 체크해보기
            insertData(getString(DBserverAddress) + "increaseHit.php");
        }
    }

    public class GetData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result == null) {
                // 결과 값 없을 경우 빈 페이지지
            } else {
                reviewList = result;
                showReviewList();
                reviewAdapter = new ReviewAdapter(getActivity(), reviewItemsArrayList);
                reviewListView.setAdapter(reviewAdapter);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            return getDB(params[0]);
        }

        private void showReviewList() {

            try {
                reviewItemsArrayList = new ArrayList<ReviewItems>();

                JSONObject jsonObject = new JSONObject(reviewList);

                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject item = jsonArray.getJSONObject(i);

                    //없는건 나중에 넣자.
                    int review_idx = Integer.parseInt(item.getString(TAG_review_idx));
                    int facility_idx = Integer.parseInt(item.getString(TAG_facility_idx));
                    String date = item.getString(TAG_date);
                    String writer = item.getString(TAG_writer);
                    String access = item.getString(TAG_access);
                    int accessResult = 0;
                    int likeN = Integer.parseInt(item.getString(TAG_likeN));
                    int toilet = Integer.parseInt(item.getString(TAG_toilet));
                    int park = Integer.parseInt(item.getString(TAG_park));
                    int elev = Integer.parseInt(item.getString(TAG_elev));
                    int tabl = Integer.parseInt(item.getString(TAG_tabl));
                    int grade = Integer.parseInt(item.getString(TAG_grade));
                    String gradeResult = null;
                    String comment = item.getString(TAG_comment);

                    switch (grade) {
                        case 0:
                            gradeResult = "☆☆☆☆☆";
                            break;
                        case 1:
                            gradeResult = "★☆☆☆☆";
                            break;
                        case 2:
                            gradeResult = "★★☆☆☆";
                            break;
                        case 3:
                            gradeResult = "★★★☆☆";
                            break;
                        case 4:
                            gradeResult = "★★★★☆";
                            break;
                        case 5:
                            gradeResult = "★★★★★";
                            break;
                    }

                    //toilet 유무
                    if (toilet == 0)
                        toilet = NULL;
                    else
                        toilet = R.drawable.toilet;

                    //접근가능성-가능, 문턱(th), 계단(st)
                    if (access.equals('Y'))
                        accessResult = R.drawable.entrance;
                    else if (access.equals('t'))
                        accessResult = NULL;
                    else
                        accessResult = NULL;

                    //주차공간 유무 - 사진 바꾸기..
                    if (park == 0)
                        park = NULL;
                    else
                        park = R.drawable.parking;

                    //elev
                    if (elev == 0)
                        elev = NULL;
                    else
                        elev = NULL;

                    //table 유무확인
                    if (tabl == 0)
                        tabl = NULL;
                    else
                        tabl = R.drawable.table;

                    reviewItemsArrayList.add(new ReviewItems(R.drawable.starbucks1, writer, date, toilet, accessResult, park, tabl, gradeResult, comment, R.drawable.heart_filled, likeN));
                }

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

    String getDB(String URL) {
        String errorString = null;
        String serverURL = URL;

        try {
            URL url = new URL(serverURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.connect();

            int responseStatusCode = httpURLConnection.getResponseCode();
            Log.d(TAG, "response code - " + responseStatusCode);

            InputStream inputStream;
            if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
            } else {
                inputStream = httpURLConnection.getErrorStream();
            }

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            bufferedReader.close();

            return sb.toString().trim();

        } catch (Exception e) {

            Log.d(TAG, "DB connection error", e);
            errorString = e.toString();

            return null;
        }
    }

    String insertData(String URL) {

        String phpAddress = URL;

        try {
            String link = phpAddress;
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(link));
            HttpResponse response = client.execute(request);
            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer sb = new StringBuffer("");
            String line = "";

            while ((line = in.readLine()) != null) {
                sb.append(line);
                break;
            }
            in.close();

        } catch (Exception e) {
            Log.d("errorLog:", e.getMessage());
        }

        return "";
    }

}
