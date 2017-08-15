package org.milal.wheeliric;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.appevents.AppEventsLogger;

public class LoginActivity extends AppCompatActivity {
    public static int APP_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AccessToken accessToken = AccountKit.getCurrentAccessToken();
        if (accessToken != null) {
            // if previously logged in, proceed to the account activity
            launchAccountActivity();
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // confirm that this response matches your request
        if (requestCode == APP_REQUEST_CODE) {
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (loginResult.getError() != null) {
                // display login error
                String toastMessage = loginResult.getError().getErrorType().getMessage();
                Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
            } else if (loginResult.getAccessToken() != null) {
                // on successful login, proceed to the account activity
                launchAccountActivity();
                //launchWheeliric();
            }
        }
    }

    private void onLogin(final LoginType loginType){
        //create intent for the Account Kit activity 여기 오타 주의! AccountActivitiy 아니고 AccountKitActivity
        final Intent intent = new Intent(this, AccountKitActivity.class);

        //configure login type and response type
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder = new AccountKitConfiguration.AccountKitConfigurationBuilder(loginType, AccountKitActivity.ResponseType.TOKEN);

        final AccountKitConfiguration configuration = configurationBuilder.build();

        //launch the Account Kit Activity
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configuration);
        startActivityForResult(intent, APP_REQUEST_CODE);
    }

    public void onPhoneLogin(View view){
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("onPhoneLogin");

        onLogin(LoginType.PHONE);
    }

    public void onEmailLogin(View view){
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("onEmailLogin");
        onLogin(LoginType.EMAIL);
    }

    public void onKakaoLogin(View view){

    }

    private void launchAccountActivity(){
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
        finish();
    }

    private void launchWheeliric(){
        Intent intent = new Intent(this, SelectActivity.class);
        startActivity(intent);
        finish();
    }
}
