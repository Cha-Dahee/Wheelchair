package org.milal.wheeliric;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Yoojung on 2017-01-19.
 * 뒤로버튼이 두번 눌러졌을 때 어플 종료
 */

public class BackPressCloseHandler {

    private long backKeyPoressedTime = 0;
    private Toast toast;
    private Activity activity;

    public BackPressCloseHandler(Activity context){
        this.activity = context;
    }

    public void onBackPressed(){
        if(System.currentTimeMillis() > backKeyPoressedTime + 2000){
            backKeyPoressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }

        if(System.currentTimeMillis() <= backKeyPoressedTime + 2000){
            activity.finish();
            toast.cancel();
        }
    }

    public void showGuide(){
        toast = Toast.makeText(activity, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }
}

