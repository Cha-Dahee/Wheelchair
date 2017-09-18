package org.milal.wheeliric;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.PhoneNumber;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import static java.sql.Types.NULL;
import static org.milal.wheeliric.R.drawable.entrance;

public class AccountActivity extends AppCompatActivity {

    TextView id;
    TextView infoLabel;
    TextView info;

    String id_nickJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        id = (TextView) findViewById(R.id.id);
        infoLabel = (TextView) findViewById(R.id.info_label);
        info = (TextView) findViewById(R.id.info);

        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(final Account account) {
                //Get Account Kit ID
                String accountKitId = account.getId();

                id.setText(accountKitId);

                PhoneNumber phoneNumber = account.getPhoneNumber();
                if(account.getPhoneNumber()!=null){
                    //if the phone number is available, display it
                    String formattedPhoneNumber = formatPhoneNumber(phoneNumber.toString());
                    info.setText(formattedPhoneNumber);
                    infoLabel.setText(R.string.phone_label);
                }
                else {
                    //if the email address is available, display it.
                    String emailString = account.getEmail();
                    info.setText(emailString);
                    infoLabel.setText(R.string.email_label);
                }

            }

            @Override
            public void onError(final AccountKitError error) {
                //display error
                String toastMessage = error.getErrorType().getMessage();
                Toast.makeText(AccountActivity.this, toastMessage, Toast.LENGTH_LONG).show();
                Toast.makeText(AccountActivity.this, "is it?", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onLogout(View view){
        //logout of Account Kits
        AccountKit.logOut();
        launchLoginActivity();
    }

    public void onContinue(View view){
        launchWheeliric();
    }

    private void launchLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void launchWheeliric(){
        Intent intent = new Intent(this, SelectActivity.class);
        startActivity(intent);
        finish();
    }

    private String formatPhoneNumber(String phoneNumber){
        //helper method to format the phone number for display
        try{
            PhoneNumberUtil pnu = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber pn = pnu.parse(phoneNumber, Locale.getDefault().getCountry());
            phoneNumber = pnu.format(pn, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
        }
        catch(NumberParseException e){
            e.printStackTrace();
        }
        return phoneNumber;
    }


    //나중에 getDataClass따로 빼내기.
    public class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressDialog = ProgressDialog.show(getActivity(), "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            progressDialog.dismiss();
            //mTextViewResult.setText(result);
            Log.d("account: ", "response is...!! - " + result);

            if (result == null){
                //    mTextViewResult.setText(errorString);
            }
            else {

                id_nickJson = result;
                showResult();
                //여기 다시 체크해보기.
                //reviewAdapter = new ReviewAdapter(getActivity(), reviewItemsArrayList);
                //reviewListView.setAdapter(reviewAdapter);
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
                Log.d("BackGroundCheck", "response code - " + responseStatusCode);

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

                Log.d("error", "InsertData: Error ", e);
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
                        accessResult= entrance;
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
}
