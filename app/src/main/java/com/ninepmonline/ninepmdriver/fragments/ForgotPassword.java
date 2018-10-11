package com.ninepmonline.ninepmdriver.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.ninepmonline.ninepmdriver.BaseScreen;
import com.ninepmonline.ninepmdriver.R;
import com.ninepmonline.ninepmdriver.WebServices.URL;
import com.ninepmonline.ninepmdriver.helper.Constants;
import com.ninepmonline.ninepmdriver.requests.RequestCompleteListener;
import com.ninepmonline.ninepmdriver.requests.VolleyRequest;
import org.json.JSONObject;

public class ForgotPassword extends Activity implements RequestCompleteListener<JSONObject> {
    Button button_enter;
    EditText e_confirm;
    EditText e_new;
    EditText e_verification;
    EditText edittext;
    ImageView imageview_email;
    RelativeLayout r_firstheader;
    RelativeLayout r_headers;
    RelativeLayout r_verification;
    RelativeLayout reset_password;
    RelativeLayout rl_enter_codes;
    TextView textview;
    TextView textview_cancel;
    TextView textview_cancels;
    TextView textview_checks;
    TextView textview_confirm_resetpassword;
    TextView textview_header;
    TextView textview_headers;
    TextView textview_new_resetpassword;
    TextView textview_notes;
    TextView textview_save;
    TextView textview_saves;
    TextView textview_verification_resetpassword;
    private String verification_code = "";
    OnClickListener viewlisteners = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_cancel:
                    try {
                        ((InputMethodManager) ForgotPassword.this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
                    } catch (Exception e) {
                    }
                    ForgotPassword.this.finish();
                    return;
                case R.id.tv_save:
                    try {
                        ((InputMethodManager) ForgotPassword.this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
                    } catch (Exception e2) {
                    }
                    ForgotPassword.this.validations();
                    return;
                case R.id.tv_cancels:
                    try {
                        ((InputMethodManager) ForgotPassword.this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
                    } catch (Exception e3) {
                    }
                    ForgotPassword.this.finish();
                    return;
                case R.id.tv_saves:
                    try {
                        ((InputMethodManager) ForgotPassword.this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
                    } catch (Exception e4) {
                    }
                    ForgotPassword.this.verifications();
                    return;
                case R.id.btn_enter:
                    ForgotPassword.this.rl_enter_codes.setVisibility(View.GONE);
                    ForgotPassword.this.reset_password.setVisibility(View.GONE);
                    ForgotPassword.this.r_verification.setVisibility(View.VISIBLE);
                    ForgotPassword.this.r_headers.setVisibility(View.GONE);
                    ForgotPassword.this.r_firstheader.setVisibility(View.GONE);
                    return;
                default:
                    return;
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        initView();
        setlisteners();
    }

    void initView() {
        this.rl_enter_codes = (RelativeLayout) findViewById(R.id.rl_enter_code);
        this.textview_cancel = (TextView) findViewById(R.id.tv_cancel);
        this.textview_header = (TextView) findViewById(R.id.tv_header_title);
        this.textview_save = (TextView) findViewById(R.id.tv_save);
        this.edittext = (EditText) findViewById(R.id.edit_email_value);
        this.textview_checks = (TextView) findViewById(R.id.tv_center_note);
        this.textview_notes = (TextView) findViewById(R.id.tv_gotoemail_msg);
        this.imageview_email = (ImageView) findViewById(R.id.img_email);
        this.button_enter = (Button) findViewById(R.id.btn_enter);
        this.textview_verification_resetpassword = (TextView) findViewById(R.id.textview_verification_resetpassword);
        this.textview_new_resetpassword = (TextView) findViewById(R.id.textview_new_resetpassword);
        this.textview_confirm_resetpassword = (TextView) findViewById(R.id.textview_confirm_resetpassword);
        this.e_verification = (EditText) findViewById(R.id.edittext_verification_resetpasword);
        this.e_new = (EditText) findViewById(R.id.edittext_new_resetpasword);
        this.e_confirm = (EditText) findViewById(R.id.edittext_confirm_resetpasword);
        this.r_verification = (RelativeLayout) findViewById(R.id.rl_verification);
        this.r_headers = (RelativeLayout) findViewById(R.id.reset_password_headers);
        this.r_firstheader = (RelativeLayout) findViewById(R.id.reset_password_header);
        this.textview_cancels = (TextView) findViewById(R.id.tv_cancels);
        this.textview_headers = (TextView) findViewById(R.id.tv_header_titles);
        this.textview_saves = (TextView) findViewById(R.id.tv_saves);
        this.reset_password = (RelativeLayout) findViewById(R.id.rl_write_email);
    }

    void setlisteners() {
        this.textview_cancel.setOnClickListener(this.viewlisteners);
        this.textview_save.setOnClickListener(this.viewlisteners);
        this.button_enter.setOnClickListener(this.viewlisteners);
        this.textview_cancels.setOnClickListener(this.viewlisteners);
        this.textview_saves.setOnClickListener(this.viewlisteners);
        TextWatcher tx = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                if (ForgotPassword.this.edittext.getText().toString().equals("")) {
                    ForgotPassword.this.textview_save.setEnabled(false);
                    ForgotPassword.this.textview_save.setTextColor(ForgotPassword.this.getResources().getColor(R.color.light_gray));
                    return;
                }
                ForgotPassword.this.textview_save.setEnabled(true);
                ForgotPassword.this.textview_save.setTextColor(ForgotPassword.this.getResources().getColor(R.color.white));
            }
        };
        TextWatcher ty = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                if (ForgotPassword.this.e_verification.getText().toString().equals("") || ForgotPassword.this.e_new.getText().toString().equals("") || ForgotPassword.this.e_confirm.getText().toString().equals("")) {
                    ForgotPassword.this.textview_saves.setEnabled(false);
                    ForgotPassword.this.textview_saves.setTextColor(ForgotPassword.this.getResources().getColor(R.color.light_gray));
                    return;
                }
                ForgotPassword.this.textview_saves.setEnabled(true);
                ForgotPassword.this.textview_saves.setTextColor(ForgotPassword.this.getResources().getColor(R.color.white));
            }
        };
        if (this.edittext.getText().toString().equals("")) {
            this.textview_save.setEnabled(false);
            this.textview_save.setTextColor(getResources().getColor(R.color.light_gray));
        } else {
            this.textview_save.setEnabled(true);
            this.textview_save.setTextColor(getResources().getColor(R.color.white));
        }
        if (this.e_verification.getText().toString().equals("") || this.e_new.getText().toString().equals("") || this.e_confirm.getText().toString().equals("")) {
            this.textview_saves.setEnabled(false);
            this.textview_saves.setTextColor(getResources().getColor(R.color.light_gray));
        } else {
            this.textview_saves.setEnabled(true);
            this.textview_saves.setTextColor(getResources().getColor(R.color.white));
        }
        this.edittext.addTextChangedListener(tx);
        this.e_verification.addTextChangedListener(ty);
        this.e_new.addTextChangedListener(ty);
        this.e_confirm.addTextChangedListener(ty);
        this.textview_saves.setText("Save");
    }

    void initdata() {
        this.textview_cancel.setTypeface(Typeface.createFromAsset(getAssets(), "PROXIMANOVA-REGULAR.OTF"));
        this.textview_header.setTypeface(Typeface.createFromAsset(getAssets(), "PROXIMANOVA-REGULAR.OTF"));
        this.textview_save.setTypeface(Typeface.createFromAsset(getAssets(), "PROXIMANOVA-REGULAR.OTF"));
        this.textview_checks.setTypeface(Typeface.createFromAsset(getAssets(), "PROXIMANOVA-REGULAR.OTF"));
        this.textview_notes.setTypeface(Typeface.createFromAsset(getAssets(), "PROXIMANOVA-REGULAR.OTF"));
        this.textview_verification_resetpassword.setTypeface(Typeface.createFromAsset(getAssets(), "PROXIMANOVA-REGULAR.OTF"));
        this.textview_new_resetpassword.setTypeface(Typeface.createFromAsset(getAssets(), "PROXIMANOVA-REGULAR.OTF"));
        this.textview_confirm_resetpassword.setTypeface(Typeface.createFromAsset(getAssets(), "PROXIMANOVA-REGULAR.OTF"));
        this.textview_cancels.setTypeface(Typeface.createFromAsset(getAssets(), "PROXIMANOVA-REGULAR.OTF"));
        this.textview_saves.setTypeface(Typeface.createFromAsset(getAssets(), "PROXIMANOVA-REGULAR.OTF"));
        this.textview_headers.setTypeface(Typeface.createFromAsset(getAssets(), "PROXIMANOVA-REGULAT.OTF"));
        this.button_enter.setTypeface(Typeface.createFromAsset(getAssets(), "PROXIMANOVA-REGULAR.OTF"));
    }

    void checkEmail() {
        JSONObject js = new JSONObject();
        try {
            js.put("email", this.edittext.getText().toString());
        } catch (Exception e) {
        }
        new VolleyRequest(this, this).makeRequest(1, URL.CHECKEMAIL.getUrl(), null, null, "", "CHECKEMAIL", true);
    }

    void ForgotPassword() {
        JSONObject js = new JSONObject();
        try {
            js.put("email", this.edittext.getText().toString());
        } catch (Exception e) {
        }
        new VolleyRequest(this, this).makeRequest(1, URL.FORGOTPASSWORD.getUrl(), null, null, "", "forgot password", true);
    }

    void validations() {
        if (!Constants.validateEmail(this.edittext.getText().toString())) {
            Constants.showMessage("Please enter valid email", this);
        } else if (Constants.isInternetOn(getApplicationContext())) {
            checkEmail();
        } else {
            Constants.errordialogs("Alert", "Please Connect to Internet", this);
        }
    }

    public void onTaskComplete(String tag, JSONObject response) {
        try {
            if (tag.equals("forgot password")) {
                Log.v("LogView", "forgot password res : " + response.toString());
                if (response.getString("success").equals("true")) {
                    this.verification_code = response.getString("data");
                    Log.v("LogView", "verification_code : " + this.verification_code);
                    this.rl_enter_codes.setVisibility(View.VISIBLE);
                    this.textview_save.setVisibility(View.GONE);
                    this.reset_password.setVisibility(View.GONE);
                    this.textview_save.setEnabled(false);
                    this.textview_notes.setText("We have sent an email to you at " + this.edittext.getText().toString() + " " + ". It has a magical code  that will sign you in.");
                    Constants.showMessage("Verification code has been sent to your mail", this);
                    return;
                }
                Constants.showMessage("Email doesn't match", this);
            } else if (tag.equals("CHECKEMAIL")) {
                Log.v("LogView", "forgot password res 1 : " + response.toString());
                if (!response.getString("success").equals("true")) {
                    Constants.showMessage(response.getJSONArray("errors").getJSONObject(0).getString("message"), this);
                } else if (response.getJSONObject("data").getString(Constants.USER_TYPE).equals("customer")) {
                    Constants.showMessage("Invalid email", this);
                } else if (response.getJSONObject("data").getString("account_type").equals("mail")) {
                    ForgotPassword();
                } else {
                    Constants.showMessage("Invalid email", this);
                }
            } else {
                if (response.getString("success").equals("true")) {
                    Constants.showMessage("Password successfully updated", this);
                    Intent intent = new Intent(this, BaseScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    return;
                }
                Constants.showMessage(response.getJSONArray("errors").getJSONObject(0).getString("message"), this);
                this.e_new.setText("");
                this.e_confirm.setText("");
            }
        } catch (Exception e) {
        }
    }

    void verifications() {
        String str_verification = this.e_verification.getText().toString().trim();
        String str_newpassword = this.e_new.getText().toString().trim();
        String str_confirm_newpassword = this.e_confirm.getText().toString().trim();
        JSONObject js = new JSONObject();
        try {
            js.put("verify_token", str_verification);
            js.put("password", str_newpassword);
            js.put("confirm_password", str_confirm_newpassword);
        } catch (Exception e) {
        }
        if (!this.verification_code.equals(str_verification)) {
            Constants.showMessage("Please enter correct verification code", this);
        } else if (str_newpassword.length() < 6 || str_confirm_newpassword.length() < 6) {
            if (str_newpassword.length() < 6) {
                Constants.showMessage("Please enter minimum 6 characters in new password", this);
            } else {
                Constants.showMessage("Please enter minimum 6 characters in confirm new password", this);
            }
        } else if (Constants.isInternetOn(getApplicationContext())) {
            new VolleyRequest(this, this).makeRequest(Request.Method.POST, URL.VERIFICATIONCODES.getUrl(), null, null, "", "Reset", true);
        } else {
            Constants.errordialogs("Alert", "Please Connect to Internet", this);
        }
    }
}
