package com.ninepmonline.ninepmdriver;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.ninepmonline.ninepmdriver.helper.Constants;

public class BaseScreen extends AppCompatActivity {
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.basescreen);
        try {
            long SPLASH_TIME = 4000;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    preferences = getSharedPreferences(Constants.INSTAFRESH, MODE_PRIVATE);
                    if (preferences.getString(Constants.ACCESS_TOKEN, "").equals("")) {
                        startActivity(new Intent(BaseScreen.this, Login.class));
                        finish();
                        return;
                    }
                    Log.v("ShowView", preferences.getString(Constants.driverid, ""));
                    startActivity(new Intent(BaseScreen.this, NavigationActivity.class));
                    finish();
                }
            }, SPLASH_TIME);
        } catch (Exception e) {
        }
    }
}
