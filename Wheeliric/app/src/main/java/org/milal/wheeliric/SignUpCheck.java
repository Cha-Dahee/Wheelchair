package org.milal.wheeliric;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Dahee Joy Cha on 2017-09-18.
 */

public class SignUpCheck extends AsyncTask<String, Void, String> {

    private String DBserverAddress;

    private String id = "";

    private String nick = "";
    private int point = 10;
    private String sex = "";
    private String wheelType = "";

    private int isNewbie = 0;

    AlertDialog.Builder signUpDialog;
    private Context context;

    //사용자가 dialog입력할때까지 db Input은 대기합니다.
    final CountDownLatch signal = new CountDownLatch(1);

    public SignUpCheck(Context context) {
        this.context = context;
    }

    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            id = params[0];
            DBserverAddress = params[1];

            String link = DBserverAddress + "checkNewMember.php?ID=" + id;

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
            isNewbie = Integer.parseInt(sb.toString());

        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }

        return "";
    }

    protected void onPostExecute(String result) {
        //회원이 아닐 경우.
        if (isNewbie == -1) {
            showSignUpDialog();
            signUpDialog.show();
        }
        //회원일 경우
        else{
            signal.countDown();
        }
    }

    public void showSignUpDialog() {
        String infoSet[];
        //회원가입 다이얼로그 생성
        signUpDialog = new AlertDialog.Builder(context);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        signUpDialog.setTitle("회원가입");

        final EditText nickInput = new EditText(context);
        nickInput.setHint("닉네임");
        layout.addView(nickInput);

        final EditText sexInput = new EditText(context); // M 혹은 W
        sexInput.setHint("성별: 여자는 W, 남자는 M");
        layout.addView(sexInput);

        final EditText wheelTypeInput = new EditText(context); //auto, manual, N/A
        wheelTypeInput.setHint("휠체어 종류: auto, manual, N/A");
        layout.addView(wheelTypeInput);

        signUpDialog.setView(layout);

        signUpDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            //temp 변수는 함수의 기본형을 맞추기 위함이며 사실 필요 X.
            public void onClick(DialogInterface dialog, int temp) {
                nick = nickInput.getText().toString();
                sex = sexInput.getText().toString();
                wheelType = wheelTypeInput.getText().toString();

                dialog.dismiss(); //확인 버튼 누르면 닫기.
                signal.countDown();
            }
        });

        signUpDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            //temp 변수는 함수의 기본형을 맞추기 위함이며 사실 필요 X.
            public void onClick(DialogInterface dialog, int temp) {
                dialog.dismiss(); //확인 버튼 누르면 닫기.
            }
        });
    }

    public class putNewbieDB extends AsyncTask<String, Void, String>{
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                signal.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < 2; i++) {

                String phplist[] =
                        {
                                DBserverAddress+"signUp.php?ID=" + id +"&nick="+nick,
                                DBserverAddress + "signUp2.php?nick=" + nick + "&point=10&sex=" + sex + "&wheelchair=" + wheelType
                        };
                try {
                    String link = phplist[i];
                    //URL url = new URL(link);
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
                    Log.d("errorLog:",e.getMessage());
                }
            }
            return "";
        }

        protected void onPostExecute(String result) {}
    }


}