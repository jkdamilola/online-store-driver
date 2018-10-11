package com.ninepmonline.ninepmdriver;

import android.preference.Preference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.LocationManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.android.volley.Request;
import com.ninepmonline.ninepmdriver.WebServices.URL;
import com.ninepmonline.ninepmdriver.fragments.Accepteds;
import com.ninepmonline.ninepmdriver.fragments.Accounts;
import com.ninepmonline.ninepmdriver.fragments.Help;
import com.ninepmonline.ninepmdriver.fragments.Upcomings;
import com.ninepmonline.ninepmdriver.helper.Constants;
import com.ninepmonline.ninepmdriver.helper.MyService;
import com.ninepmonline.ninepmdriver.helper.ResideMenu;
import com.ninepmonline.ninepmdriver.helper.ResideMenu.OnMenuListener;
import com.ninepmonline.ninepmdriver.helper.ResideMenuItem;
import com.ninepmonline.ninepmdriver.requests.RequestCompleteListener;
import com.ninepmonline.ninepmdriver.requests.VolleyRequest;
import org.json.JSONObject;

public class NavigationActivity extends AppCompatActivity implements RequestCompleteListener<JSONObject>, OnClickListener {
    public ResideMenuItem item2;
    public static ResideMenu resideMenu;
    private Dialog dialog;
    String driverid;
    private ResideMenuItem item1;
    private ResideMenuItem item_Account;
    private ResideMenuItem item_Logout;
    private ResideMenuItem items;
    JSONObject js = new JSONObject();
    private NavigationActivity mContext;
    private OnMenuListener menuListener = new OnMenuListener() {
        public void openMenu() {
        }

        public void closeMenu() {
        }
    };
    private SharedPreferences preferences;

    @Override
    public View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(view, str, context, attributeSet);
    }

    @Override
    public View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(str, context, attributeSet);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation);
        preferences = getSharedPreferences(Constants.INSTAFRESH, MODE_PRIVATE);
        driverid = preferences.getString(Constants.driverid, "0");
        
        mContext = this;
        if (((LocationManager) getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            startService(new Intent(this, MyService.class));
        }
        setUpMenu();
        changeFragment(new Upcomings());
    }

    protected void onResume() {
        super.onResume();
        if (((LocationManager) getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            startService(new Intent(this, MyService.class));
        }
    }

    private void setUpMenu() {
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.side_menus);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        resideMenu.setScaleValue(0.6f);
        item1 = new ResideMenuItem(this, R.drawable.upcoming_delivery_icon, "Upcoming Delivery", R.drawable.menu_line);
        item2 = new ResideMenuItem(this, R.drawable.acceped_delivery_icon, "Accepted Delivery", R.drawable.menu_line);
        item_Account = new ResideMenuItem(this, R.drawable.logouticon, "Logout", R.drawable.menu_line);
        items = new ResideMenuItem(this, R.drawable.help_icon, "Help", R.drawable.menu_line);
        item1.setOnClickListener(this);
        item2.setOnClickListener(this);
        item_Account.setOnClickListener(this);
        items.setOnClickListener(this);
        resideMenu.addMenuItem(item1, 0);
        resideMenu.addMenuItem(item2, 0);
        resideMenu.addMenuItem(items, 0);
        resideMenu.addMenuItem(item_Account, 0);
        resideMenu.setSwipeDirectionDisable(1);
    }

    public void onClick(View v) {
        if (v == this.item1) {
            Bundle b = new Bundle();
            b.putString("driverid", this.driverid);
            Upcomings upcomming = new Upcomings();
            upcomming.setArguments(b);
            changeFragment(upcomming);
        } else if (v == item2) {
            changeFragment(new Accepteds());
        } else if (v == this.item_Account) {
            // changeFragment(new Accounts());
            Editor editors = this.preferences.edit();
            editors.putString(Constants.ACCESS_TOKEN, "");
            editors.putString(Constants.LOGIN, "");
            editors.putString(Constants.POSTAL_CODE, "");
            editors.putString(Constants.lATITUDE, "");
            editors.putString(Constants.LONGITUDE, "");
            editors.commit();
            Intent intent = new Intent(this, BaseScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else if (v == this.items) {
           changeFragment(new Help());
        }
        resideMenu.closeMenu();
    }

    public void changeFragment(Fragment targetFragment) {
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, targetFragment, "fragment").setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    public ResideMenu getResideMenu() {
        return resideMenu;
    }

    public void onTaskComplete(String tag, JSONObject response) {
        String success = "";
        try {
            if (response.getString("success").equals("true")) {
                Editor editors = this.preferences.edit();
                editors.putString(Constants.ACCESS_TOKEN, "");
                editors.putString(Constants.LOGIN, "");
                editors.putString(Constants.POSTAL_CODE, "");
                editors.putString(Constants.LOGIN, "");
                editors.putString(Constants.LOGIN, "");
                editors.commit();
                Intent intent = new Intent(this, BaseScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                this.dialog.dismiss();
            } else if (!success.equals("false")) {
            } else {
                if (response.getJSONArray("errors").getJSONObject(0).getString("message").equals("Invalid Access Token.")) {
                    Constants.dialogs("Alert", "Invalid Access Token.", null, this, "");
                } else {
                    Constants.dialogs("Alert", response.getJSONArray("errors").getJSONObject(0).getString("message"), null, this, "");
                }
            }
        } catch (Exception e) {
        }
    }

    public void onBackPressed() {
        try {
            if (!((BaseContainerFragments) getSupportFragmentManager().findFragmentByTag("fragment")).popFragment()) {
                finish();
            }
        } catch (Exception e) {
            finish();
        }
    }

    void logout() {
        try {
            this.js.put("access_token", this.preferences.getString(Constants.ACCESS_TOKEN, ""));
        } catch (Exception e) {
        }
        new VolleyRequest(this, this).makeRequest(Request.Method.POST, URL.LOGOUT.getUrl(), null, null, "", "LOGSOUT", true);
    }
}
