package com.ninepmonline.ninepmdriver.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.ninepmonline.ninepmdriver.R;
import com.ninepmonline.ninepmdriver.WebServices.URL;
import com.ninepmonline.ninepmdriver.helper.Constants;
import com.ninepmonline.ninepmdriver.requests.RequestCompleteListener;
import com.ninepmonline.ninepmdriver.requests.VolleyRequest;
import org.json.JSONObject;

public class PasswordChange extends Activity implements RequestCompleteListener<JSONObject> {
    private String confirm_New_Password = "";
    EditText confirmpassword;
    private String current_Password = "";
    private Dialog dialog;
    OnClickListener listeners = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_save:
                    try {
                        ((InputMethodManager) PasswordChange.this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
                    } catch (Exception e) {
                    }
                    if (PasswordChange.this.newpassword.getText().toString().trim().length() < 6) {
                        Constants.showMessage("New Password should be atleast 6 character long.", PasswordChange.this);
                        return;
                    } else if (PasswordChange.this.confirmpassword.getText().toString().trim().length() < 6) {
                        Constants.showMessage("Confirm Password should be atleast 6 character long.", PasswordChange.this);
                        return;
                    } else if (PasswordChange.this.newpassword.getText().toString().trim().equals(PasswordChange.this.confirmpassword.getText().toString().trim())) {
                        PasswordChange.this.new_Password = PasswordChange.this.newpassword.getText().toString().trim();
                        PasswordChange.this.confirm_New_Password = PasswordChange.this.confirmpassword.getText().toString().trim();
                        PasswordChange.this.dialog = new Dialog(PasswordChange.this);
                        PasswordChange.this.dialog.requestWindowFeature(1);
                        PasswordChange.this.dialog.setCancelable(false);
                        PasswordChange.this.dialog.setCanceledOnTouchOutside(false);
                        PasswordChange.this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                        PasswordChange.this.dialog.setContentView(R.layout.current_password_dialog);
                        TextView textview = (TextView) PasswordChange.this.dialog.findViewById(R.id.textview__header);
                        TextView textview_messages = (TextView) PasswordChange.this.dialog.findViewById(R.id.textview__messages);
                        final EditText edit_current_password = (EditText) PasswordChange.this.dialog.findViewById(R.id.edit_current_password);
                        Button submit = (Button) PasswordChange.this.dialog.findViewById(R.id.button_ok);
                        Button cancel = (Button) PasswordChange.this.dialog.findViewById(R.id.button_cancel);
                        submit.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    ((InputMethodManager) PasswordChange.this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(edit_current_password.getWindowToken(), 0);
                                } catch (Exception e) {
                                }
                                if (edit_current_password.getText().toString().trim().equals("") || edit_current_password.getText().toString().trim().length() < 6) {
                                    Constants.showMessage("Your current password is invalid", PasswordChange.this);
                                    return;
                                }
                                PasswordChange.this.dialog.dismiss();
                                PasswordChange.this.current_Password = edit_current_password.getText().toString().trim();
                                if (Constants.isInternetOn(PasswordChange.this)) {
                                    PasswordChange.this.passwordchange();
                                } else {
                                    Constants.errordialogs("Alert", "Please Connect to Internet", PasswordChange.this);
                                }
                            }
                        });
                        cancel.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    ((InputMethodManager) PasswordChange.this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(edit_current_password.getWindowToken(), 0);
                                } catch (Exception e) {
                                }
                                PasswordChange.this.dialog.dismiss();
                            }
                        });
                        textview.setTypeface(Typeface.createFromAsset(PasswordChange.this.getAssets(), "PROXIMANOVA-SEMIBOLD.OTF"));
                        textview_messages.setTypeface(Typeface.createFromAsset(PasswordChange.this.getAssets(), "PROXIMANOVA-REGULAR.OTF"));
                        edit_current_password.setTypeface(Typeface.createFromAsset(PasswordChange.this.getAssets(), "PROXIMANOVA-REGULAR.OTF"));
                        submit.setTypeface(Typeface.createFromAsset(PasswordChange.this.getAssets(), "PROXIMANOVA-REGULAR.OTF"));
                        cancel.setTypeface(Typeface.createFromAsset(PasswordChange.this.getAssets(), "PROXIMANOVA-REGULAR.OTF"));
                        PasswordChange.this.dialog.show();
                        return;
                    } else {
                        Constants.showMessage("Confirm password and new Password should match.", PasswordChange.this);
                        return;
                    }
                case R.id.back:
                    try {
                        ((InputMethodManager) PasswordChange.this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
                    } catch (Exception e2) {
                    }
                    PasswordChange.this.finish();
                    return;
                default:
                    return;
            }
        }
    };
    private String new_Password = "";
    EditText newpassword;
    TextView t_cancel;
    TextView t_save;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_change);
        initview();
        setlisteners();
    }

    void initview() {
        this.newpassword = (EditText) findViewById(R.id.edittext_confirms_resetpasword);
        this.confirmpassword = (EditText) findViewById(R.id.edittext_new_resetpasword);
        this.t_cancel = (TextView) findViewById(R.id.back);
        this.t_save = (TextView) findViewById(R.id.tv_save);
        TextView textview_header = (TextView) findViewById(R.id.tv_header_title);
        this.newpassword.setTypeface(Typeface.createFromAsset(getAssets(), "PROXIMANOVA-REGULAR.OTF"));
        this.confirmpassword.setTypeface(Typeface.createFromAsset(getAssets(), "PROXIMANOVA-REGULAR.OTF"));
        this.t_cancel.setTypeface(Typeface.createFromAsset(getAssets(), "PROXIMANOVA-REGULAR.OTF"));
        this.t_save.setTypeface(Typeface.createFromAsset(getAssets(), "PROXIMANOVA-REGULAR.OTF"));
        textview_header.setTypeface(Typeface.createFromAsset(getAssets(), "PROXIMANOVA-REGULAR.OTF"));
        TextWatcher mTextWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                if (PasswordChange.this.confirmpassword.getText().toString().trim().equals("") || PasswordChange.this.confirmpassword.getText().toString().trim().length() < 6 || PasswordChange.this.newpassword.getText().toString().trim().equals("") || PasswordChange.this.newpassword.getText().toString().trim().length() < 6 || !PasswordChange.this.confirmpassword.getText().toString().trim().equals(PasswordChange.this.newpassword.getText().toString().trim())) {
                    PasswordChange.this.t_save.setEnabled(false);
                    PasswordChange.this.t_save.setTextColor(PasswordChange.this.getResources().getColor(R.color.light_gray));
                    return;
                }
                PasswordChange.this.t_save.setEnabled(true);
                PasswordChange.this.t_save.setTextColor(-1);
            }
        };
        if (this.confirmpassword.getText().toString().trim().equals("") || this.confirmpassword.getText().toString().trim().length() < 6 || this.newpassword.getText().toString().trim().equals("") || this.newpassword.getText().toString().trim().length() < 6 || !this.confirmpassword.getText().toString().trim().equals(this.newpassword.getText().toString().trim())) {
            this.t_save.setEnabled(false);
            this.t_save.setTextColor(getResources().getColor(R.color.light_gray));
        } else {
            this.t_save.setEnabled(true);
            this.t_save.setTextColor(-1);
        }
        this.newpassword.addTextChangedListener(mTextWatcher);
        this.confirmpassword.addTextChangedListener(mTextWatcher);
    }

    void setlisteners() {
        this.t_cancel.setOnClickListener(this.listeners);
        this.t_save.setOnClickListener(this.listeners);
    }

    void passwordchange() {
        SharedPreferences preferences = getSharedPreferences(Constants.INSTAFRESH, 0);
        String str_current = this.current_Password;
        String str_new = this.new_Password;
        String str_confirm = this.confirm_New_Password;
        JSONObject js = new JSONObject();
        try {
            js.put("current_password", str_current);
            js.put("access_token", preferences.getString(Constants.ACCESS_TOKEN, "0"));
            js.put("password", str_new);
            js.put("confirm_password", str_confirm);
            System.out.println("vvvvvvvvvvvvvvv" + preferences.getString(Constants.ACCESS_TOKEN, "0"));
        } catch (Exception e) {
        }
        Log.v("LogView", "Change Password sending json :" + js.toString());
        new VolleyRequest(this, this).makeRequest(1, URL.UPDATE_PASSWORDS.getUrl(), null, null, "", "PasswordChange", true);
    }

    public void onTaskComplete(String tag, JSONObject response) {
        try {
            if (response.getString("success").equals("true")) {
                Constants.showMessage("Password Changed", this);
                finish();
            } else if (response.getJSONArray("errors").getJSONObject(0).getString("message").equals("Invalid Access Token.")) {
                Constants.dialogs("Alert", response.getJSONArray("errors").getJSONObject(0).getString("message"), null, this, "");
            } else {
                Constants.showMessage(response.getJSONArray("errors").getJSONObject(0).getString("message"), this);
            }
        } catch (Exception e) {
        }
    }
}
