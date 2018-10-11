package com.ninepmonline.ninepmdriver.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.ninepmonline.ninepmdriver.R;
import com.ninepmonline.ninepmdriver.WebServices.URL;
import com.ninepmonline.ninepmdriver.helper.Constants;
import com.ninepmonline.ninepmdriver.requests.RequestCompleteListener;
import com.ninepmonline.ninepmdriver.requests.VolleyRequest;
import org.json.JSONException;
import org.json.JSONObject;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN;

public class UpdateAccount extends Activity implements RequestCompleteListener<JSONObject> {
    String emailaddress;
    private EditText et_address;
    private EditText et_first_name;
    private EditText et_last_name;
    private EditText et_phone_number;
    String fname;
    String id;
    private ImageView iv_back_arow;
    OnClickListener listeners = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_save:
                    try {
                        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(UpdateAccount.this.getCurrentFocus().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    } catch (Exception e) {
                    }
                    validation();
                    return;
                case R.id.iv_back_arow:
                    finish();
                    return;
                case R.id.tv_change_password:
                    startActivity(new Intent(UpdateAccount.this, PasswordChange.class));
                    return;
                default:
                    return;
            }
        }
    };
    String lname;
    String phoneno;
    private RelativeLayout rl_change_password;
    private TextView tv_change_password;
    private TextView tv_personal_info;
    private TextView tv_save;
    private View v1;
    private View v2;

    @Nullable
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_accounts);
        SharedPreferences preferences = getSharedPreferences(Constants.INSTAFRESH, 0);
        getWindow().setSoftInputMode(SOFT_INPUT_STATE_HIDDEN);
        initView();
        setListener();
        initData();
        if (Constants.isInternetOn(this)) {
            getUserInfos();
        } else {
            Constants.errordialogs("Alert", "Please Connect to Internet", this);
        }
    }

    private void initView() {
        this.tv_personal_info = (TextView) findViewById(R.id.tv_personal_info);
        this.tv_change_password = (TextView) findViewById(R.id.tv_change_password);
        this.rl_change_password = (RelativeLayout) findViewById(R.id.rl_change_password);
        this.v1 = findViewById(R.id.v1);
        this.v2 = findViewById(R.id.v2);
        this.et_first_name = (EditText) findViewById(R.id.et_first_name);
        this.et_last_name = (EditText) findViewById(R.id.et_last_name);
        this.et_address = (EditText) findViewById(R.id.et_address);
        this.et_phone_number = (EditText) findViewById(R.id.et_phone_number);
        this.tv_save = (TextView) findViewById(R.id.tv_save);
        this.iv_back_arow = (ImageView) findViewById(R.id.iv_back_arow);
        SharedPreferences preferences = getSharedPreferences(Constants.INSTAFRESH, MODE_PRIVATE);
        if (preferences.getString("login_type", "").equals("facebook")) {
            this.et_address.setEnabled(false);
        } else if (preferences.getString("login_type", "").equals("mail")) {
            this.et_address.setEnabled(true);
        } else if (preferences.getString("login_type", "").equals("google")) {
            this.et_address.setEnabled(false);
        }
        TextWatcher mTextWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                if ((UpdateAccount.this.et_first_name.getText().toString().trim().equals(UpdateAccount.this.fname) && UpdateAccount.this.et_last_name.getText().toString().trim().equals(UpdateAccount.this.lname) && UpdateAccount.this.et_address.getText().toString().trim().equals(UpdateAccount.this.emailaddress) && UpdateAccount.this.et_phone_number.getText().toString().trim().equals(UpdateAccount.this.phoneno)) || UpdateAccount.this.et_first_name.getText().toString().trim().equals("") || UpdateAccount.this.et_last_name.getText().toString().trim().equals("") || UpdateAccount.this.et_address.getText().toString().trim().equals("") || UpdateAccount.this.et_phone_number.getText().toString().trim().equals("")) {
                    UpdateAccount.this.tv_save.setEnabled(false);
                    UpdateAccount.this.tv_save.setTextColor(UpdateAccount.this.getResources().getColor(R.color.light_gray));
                    return;
                }
                UpdateAccount.this.tv_save.setEnabled(true);
                UpdateAccount.this.tv_save.setTextColor(-1);
            }
        };
        if ((this.et_first_name.getText().toString().trim().equals(this.fname) && this.et_last_name.getText().toString().trim().equals(this.lname) && this.et_address.getText().toString().trim().equals(this.emailaddress) && this.et_phone_number.getText().toString().trim().equals(this.phoneno)) || this.et_first_name.getText().toString().trim().equals("") || this.et_last_name.getText().toString().trim().equals("") || this.et_address.getText().toString().trim().equals("") || this.et_phone_number.getText().toString().trim().equals("")) {
            this.tv_save.setEnabled(false);
            this.tv_save.setTextColor(getResources().getColor(R.color.light_gray));
        } else {
            this.tv_save.setEnabled(true);
            this.tv_save.setTextColor(-1);
        }
        this.et_first_name.addTextChangedListener(mTextWatcher);
        this.et_last_name.addTextChangedListener(mTextWatcher);
        this.et_address.addTextChangedListener(mTextWatcher);
        this.et_phone_number.addTextChangedListener(mTextWatcher);
    }

    private void setListener() {
        this.tv_save.setOnClickListener(this.listeners);
        this.iv_back_arow.setOnClickListener(this.listeners);
        this.tv_change_password.setOnClickListener(this.listeners);
    }

    private void initData() {
        this.tv_personal_info.setTypeface(Typeface.createFromAsset(getAssets(), "PROXIMANOVA-SEMIBOLD.OTF"));
        this.tv_change_password.setTypeface(Typeface.createFromAsset(getAssets(), "PROXIMANOVA-REGULAR.OTF"));
        this.et_first_name.setTypeface(Typeface.createFromAsset(getAssets(), "PROXIMANOVA-REGULAR.OTF"));
        this.et_last_name.setTypeface(Typeface.createFromAsset(getAssets(), "PROXIMANOVA-REGULAR.OTF"));
        this.et_address.setTypeface(Typeface.createFromAsset(getAssets(), "PROXIMANOVA-REGULAR.OTF"));
        this.et_phone_number.setTypeface(Typeface.createFromAsset(getAssets(), "PROXIMANOVA-REGULAR.OTF"));
    }

    private void validation() {
        if (this.et_first_name.getText().toString().trim().equals("")) {
            Constants.showMessage("Please enter first name.", this);
        } else if (this.et_last_name.getText().toString().trim().equals("")) {
            Constants.showMessage("Please enter Last name.", this);
        } else if (this.et_address.getText().toString().trim().equals("")) {
            Constants.showMessage("Please enter email id.", this);
        } else if (!Constants.validateEmail(this.et_address.getText().toString().trim())) {
            Constants.showMessage("Please enter valid email id.", this);
        } else if (this.et_phone_number.getText().toString().trim().equals("")) {
            Constants.showMessage("Please enter phone number.", this);
        } else if (this.et_phone_number.getText().toString().trim().length() < 10) {
            Constants.showMessage("Please enter atleast 10 digit phone number.", this);
        } else if (Constants.isInternetOn(this)) {
            makeUpdate();
        } else {
            Constants.errordialogs("Alert", "Please Connect to Internet", this);
        }
    }

    private void makeUpdate() {
        SharedPreferences preferences = getSharedPreferences(Constants.INSTAFRESH, 0);
        this.id = preferences.getString("user_id", "");
        JSONObject update = new JSONObject();
        try {
            update.put("firstname", this.et_first_name.getText().toString().trim());
            update.put("lastname", this.et_last_name.getText().toString().trim());
            update.put("telephone", this.et_phone_number.getText().toString().trim());
            update.put("email", this.et_address.getText().toString().trim());
            update.put(Constants.USER_TYPE, "driver");
            update.put("access_token", preferences.getString(Constants.ACCESS_TOKEN, ""));
        } catch (Exception e) {
        }
        Log.v("LogView", "Update profile Sending json : " + update.toString());
        new VolleyRequest(this, this).makeRequest(Request.Method.PUT, new StringBuilder(String.valueOf(URL.UPDATEACCOUNT.getUrl())).append("/").append(this.id).toString(), null, null, "", "updateaccount", true);
    }

    void getUserInfos() {
        SharedPreferences preferences = getSharedPreferences(Constants.INSTAFRESH, MODE_PRIVATE);
        this.id = preferences.getString("user_id", "");
        new VolleyRequest(this, this).makeRequest(Request.Method.GET, new StringBuilder(String.valueOf(URL.GETUSER.getUrl())).append("/").append(this.id).append("?").append("access_token=").append(preferences.getString(Constants.ACCESS_TOKEN, "")).toString(), null, null, "", "getuser", true);
    }

    public void onTaskComplete(String tag, JSONObject reaponse) {
        try {
            Log.v("LogView", "Update profile res : " + reaponse.toString());
            String success = reaponse.getString("success");
            JSONObject jsonobject;
            if (tag.equals("updateaccount")) {
                if (success.equals("true")) {
                    jsonobject = reaponse.getJSONObject("data");
                    this.fname = jsonobject.getString("firstname");
                    this.lname = jsonobject.getString("lastname");
                    this.emailaddress = jsonobject.getString("email");
                    this.phoneno = jsonobject.getString("telephone");
                    this.et_first_name.setText(this.fname);
                    this.et_last_name.setText(this.lname);
                    this.et_address.setText(this.emailaddress);
                    this.et_phone_number.setText(this.phoneno);
                    Gson gson = new Gson();
                    Editor editor = getSharedPreferences(Constants.INSTAFRESH, 0).edit();
                    editor.putString(Constants.LOGIN, gson.toJson(reaponse.getJSONObject("data")));
                    editor.commit();
                    Constants.showMessage("Profile updated successfully ", this);
                } else if (!success.equals("false")) {
                } else {
                    if (reaponse.getJSONArray("errors").getJSONObject(0).getString("data").equals("Invalid Access Token.")) {
                        Constants.dialogs("Alert", "Invalid Access Token.", null, this, "");
                    } else {
                        Constants.showMessage(reaponse.getJSONArray("errors").getJSONObject(0).getString("data"), this);
                    }
                }
            } else if (success.equals("true")) {
                System.out.println(new StringBuilder("success").append(success).toString());
                jsonobject = reaponse.getJSONObject("data");
                this.fname = jsonobject.getString("firstname");
                this.lname = jsonobject.getString("lastname");
                this.emailaddress = jsonobject.getString("email");
                this.phoneno = jsonobject.getString("telephone");
                this.et_first_name.setText(this.fname);
                this.et_last_name.setText(this.lname);
                this.et_address.setText(this.emailaddress);
                this.et_phone_number.setText(this.phoneno);
            } else if (!success.equals("false")) {
            } else {
                if (reaponse.getJSONArray("errors").getJSONObject(0).getString("message").equals("Invalid Access Token.")) {
                    Constants.dialogs("Alert", "Invalid Access Token.", null, this, "");
                } else {
                    Constants.dialogs("Alert", reaponse.getJSONArray("errors").getJSONObject(0).getString("message"), null, this, "");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
