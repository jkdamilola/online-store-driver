package com.ninepmonline.ninepmdriver;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ninepmonline.ninepmdriver.WebServices.URL;
import com.ninepmonline.ninepmdriver.fragments.ForgotPassword;
import com.ninepmonline.ninepmdriver.helper.Constants;
import com.ninepmonline.ninepmdriver.requests.RequestCompleteListener;
import com.ninepmonline.ninepmdriver.requests.VolleyRequest;
import org.json.JSONObject;

public class Login extends AppCompatActivity implements RequestCompleteListener<JSONObject> {
    EditText edittext_drivers_name;
    EditText edittext_drivers_password;
    TextView forgot_passwords;
    OnClickListener listeners = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.forgot_passwords:
                    startActivity(new Intent(Login.this.getApplicationContext(), ForgotPassword.class));
                    return;
                case R.id.textview_drivers_login:
                    validations();
                    return;
                default:
                    return;
            }
        }
    };
    private SharedPreferences preferences;
    String s;
    TextView textview_drivers_signins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        initview();
        setlisteners();
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setNavigationIcon((int) R.drawable.back_arow);
        toolbar.setBackgroundColor(Color.TRANSPARENT);
        toolbar.setTitleTextColor(-1);
    }

    void initview() {
        this.edittext_drivers_name = (EditText) findViewById(R.id.edit_username);
        this.edittext_drivers_password = (EditText) findViewById(R.id.edit_passwords);
        this.textview_drivers_signins = (TextView) findViewById(R.id.textview_drivers_login);
        this.forgot_passwords = (TextView) findViewById(R.id.forgot_passwords);
        this.forgot_passwords = (TextView) findViewById(R.id.forgot_passwords);
        this.edittext_drivers_name.setTypeface(Typeface.createFromAsset(getAssets(), "PROXIMANOVA-REGULAR.OTF"));
        this.edittext_drivers_password.setTypeface(Typeface.createFromAsset(getAssets(), "PROXIMANOVA-REGULAR.OTF"));
        this.textview_drivers_signins.setTypeface(Typeface.createFromAsset(getAssets(), "PROXIMANOVA-REGULAR.OTF"));
        this.forgot_passwords.setTypeface(Typeface.createFromAsset(getAssets(), "PROXIMANOVA-REGULAR.OTF"));
    }

    void setlisteners() {
        this.textview_drivers_signins.setOnClickListener(this.listeners);
        this.forgot_passwords.setOnClickListener(this.listeners);
    }

    void validations() {
        String str_username = this.edittext_drivers_name.getText().toString();
        String str_password = this.edittext_drivers_password.getText().toString();
        if (str_username.equals("")) {
            Constants.showMessage("Enter Email", this);
        } else if (str_password.equals("")) {
            Constants.showMessage("Enter Password", this);
        } else if (Constants.isInternetOn(this)) {
            login();
        } else {
            Constants.showMessage("No Internet", this);
        }
    }

    void login() {
        String str_username = this.edittext_drivers_name.getText().toString();
        String str_password = this.edittext_drivers_password.getText().toString();
        
        String url = String.format(URL.LOGIN.getUrl(), str_username, str_password);
        new VolleyRequest(this, this).makeRequest(Request.Method.GET, url, null, null, "", "LOGIN", true);
    }

    public void onTaskComplete(String tag, JSONObject response) {
        try {
            if (response.getString("status").equals("ok")) {
                JSONObject loginobject = response.getJSONObject("user");
                if (loginobject.getString("role").equals("deliveryman")) {
                    Log.v("ShowView", "Login Response JSON :" + loginobject.toString());
                    Gson gson = new Gson();
                    this.preferences = getSharedPreferences(Constants.INSTAFRESH, MODE_PRIVATE);
                    Editor editor = this.preferences.edit();
                    editor.putString(Constants.LOGIN, gson.toJson(response.getJSONObject("user"), new TypeToken<JSONObject>() {
                    }.getType()));
                    editor.putString(Constants.ACCESS_TOKEN, loginobject.getString("id"));
                    editor.putString("user_id", loginobject.getString("id"));
                    editor.putString("role", loginobject.getString("role"));
                    editor.putString(Constants.driverids, loginobject.getString("id"));
                    editor.putString(Constants.driverid, loginobject.getString("id"));
                    editor.commit();
                    Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                    intent.putExtra("driverid", loginobject.getString("id"));
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Constants.showMessage("Invalid email and password", this);
                }
            } else if (response.getString("status").equals("error")) {
                Constants.showMessage(response.getString("error"), this);
            }
        } catch (Exception e) {
            Log.v("ShowView", e.getMessage());
        }
    }
}
