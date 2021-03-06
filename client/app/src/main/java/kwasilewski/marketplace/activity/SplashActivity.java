package kwasilewski.marketplace.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import kwasilewski.marketplace.dto.user.UserData;
import kwasilewski.marketplace.retrofit.listener.ErrorListener;
import kwasilewski.marketplace.retrofit.listener.UserListener;
import kwasilewski.marketplace.retrofit.manager.UserManager;
import kwasilewski.marketplace.util.SharedPrefUtil;

public class SplashActivity extends AppCompatActivity implements UserListener, ErrorListener {

    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userManager = new UserManager(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String token = SharedPrefUtil.getInstance(this).getToken();
        if (token != null) {
            userManager.validateToken(this);
        } else {
            goToMainActivity();
        }
    }

    @Override
    protected void onPause() {
        userManager.cancelCalls();
        super.onPause();
    }

    @Override
    public void tokenValidated(UserData user) {
        tokenAuthorized(true);
    }

    @Override
    public void unauthorized(Activity activity) {
        tokenAuthorized(false);
    }

    private void tokenAuthorized(boolean authorized) {
        if (!authorized) {
            SharedPrefUtil.getInstance(this).removeUserData();
        }
        goToMainActivity();
    }

    private void goToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}
