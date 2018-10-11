package com.ninepmonline.ninepmdriver.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.ninepmonline.ninepmdriver.BaseContainerFragments;
import com.ninepmonline.ninepmdriver.BaseScreen;
import com.ninepmonline.ninepmdriver.NavigationActivity;
import com.ninepmonline.ninepmdriver.R;
import com.ninepmonline.ninepmdriver.WebServices.URL;
import com.ninepmonline.ninepmdriver.helper.Constants;
import com.ninepmonline.ninepmdriver.requests.RequestCompleteListener;
import com.ninepmonline.ninepmdriver.requests.VolleyRequest;
import org.json.JSONObject;

public class Accounts extends BaseContainerFragments implements RequestCompleteListener<JSONObject> {
    private Dialog dialog;
    Editor editor;
    private TypedArray icon_image;
    private String[] icon_name = new String[]{"Update Account", "Sign Out"};
    private ImageView iv_menu = null;
    private ListView menu_list;
    SharedPreferences preferences;
    private View rootView = null;
    int value;

    class MenuListAdapter extends BaseAdapter {
        TypedArray icon_image;
        String[] icon_name;

        public MenuListAdapter(TypedArray icon_image, String[] icon_name) {
            this.icon_image = icon_image;
            this.icon_name = icon_name;
        }

        public int getCount() {
            return this.icon_name.length;
        }

        public Object getItem(int position) {
            return Integer.valueOf(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(Accounts.this.getActivity(), R.layout.accountt_list_item, null);
            }
            ImageView iv_menu_icon = (ImageView) convertView.findViewById(R.id.image);
            TextView tv_menu_name = (TextView) convertView.findViewById(R.id.textview);
            tv_menu_name.setTypeface(Typeface.createFromAsset(Accounts.this.getActivity().getAssets(), "PROXIMANOVA-REGULAR.OTF"));
            iv_menu_icon.setImageResource(this.icon_image.getResourceId(position, -1));
            tv_menu_name.setText(this.icon_name[position]);
            return convertView;
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.accounts, null);
        this.preferences = getActivity().getSharedPreferences(Constants.INSTAFRESH, 0);
        initView();
        setListener();
        initData();
        return this.rootView;
    }

    private void initView() {
        this.menu_list = (ListView) this.rootView.findViewById(R.id.account_list);
        this.iv_menu = (ImageView) this.rootView.findViewById(R.id.iv_menu);
    }

    private void setListener() {
        this.iv_menu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationActivity.resideMenu.openMenu(0);
            }
        });
        this.menu_list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Accounts.this.startActivity(new Intent(Accounts.this.getActivity(), UpdateAccount.class));
                        return;
                    case 1:
                        Accounts.this.dialog = new Dialog(Accounts.this.getActivity());
                        Accounts.this.dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        Accounts.this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                        View t = LayoutInflater.from(Accounts.this.getActivity()).inflate(R.layout.logsout, null);
                        Accounts.this.dialog.setContentView(R.layout.logsout);
                        TextView textview = (TextView) Accounts.this.dialog.findViewById(R.id.textview__header);
                        TextView textview_messages = (TextView) Accounts.this.dialog.findViewById(R.id.textview__messages);
                        Button submit = (Button) Accounts.this.dialog.findViewById(R.id.button_ok);
                        Button cancel = (Button) Accounts.this.dialog.findViewById(R.id.button_cancel);
                        submit.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (Constants.isInternetOn(Accounts.this.getActivity())) {
                                    Accounts.this.logout();
                                } else {
                                    Constants.errordialogs("Alert", "Please Connect to Internet", Accounts.this.getActivity());
                                }
                            }
                        });
                        cancel.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Accounts.this.dialog.dismiss();
                            }
                        });
                        textview.setTypeface(Typeface.createFromAsset(Accounts.this.getActivity().getAssets(), "PROXIMANOVA-REGULAR.OTF"));
                        textview_messages.setTypeface(Typeface.createFromAsset(Accounts.this.getActivity().getAssets(), "PROXIMANOVA-REGULAR.OTF"));
                        submit.setTypeface(Typeface.createFromAsset(Accounts.this.getActivity().getAssets(), "PROXIMANOVA-REGULAR.OTF"));
                        cancel.setTypeface(Typeface.createFromAsset(Accounts.this.getActivity().getAssets(), "PROXIMANOVA-REGULAR.OTF"));
                        Accounts.this.dialog.show();
                        return;
                    default:
                        return;
                }
            }
        });
    }

    void logout() {
        JSONObject js = new JSONObject();
        try {
            js.put("access_token", this.preferences.getString(Constants.ACCESS_TOKEN, ""));
        } catch (Exception e) {
        }
        new VolleyRequest(getActivity(), this).makeRequest(Request.Method.POST, URL.LOGOUT.getUrl(), null, null, "", "", true);
    }

    private void initData() {
        this.icon_image = getResources().obtainTypedArray(R.array.icons);
        this.menu_list.setAdapter(new MenuListAdapter(this.icon_image, this.icon_name));
    }

    public void onTaskComplete(String tag, JSONObject reaponse) {
        String success = "";
        try {
            if (reaponse.getString("success").equals("true")) {
                Editor editors = this.preferences.edit();
                editors.putString(Constants.ACCESS_TOKEN, "");
                editors.putString(Constants.LOGIN, "");
                editors.putString(Constants.POSTAL_CODE, "");
                editors.putString(Constants.lATITUDE, "");
                editors.putString(Constants.LONGITUDE, "");
                editors.commit();
                Intent intent = new Intent(getActivity(), BaseScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.dialog.dismiss();
                getActivity().startActivity(intent);
                getActivity().finish();
            } else if (!reaponse.getString("success").equals("false")) {
            } else {
                if (reaponse.getJSONArray("errors").getJSONObject(0).getString("message").equals("Invalid Access Token.")) {
                    Constants.dialogs("Alert", "Invalid Access Token.", null, getActivity(), "");
                } else {
                    Constants.errordialogs("Alert", reaponse.getJSONArray("errors").getJSONObject(0).getString("message"), getActivity());
                }
            }
        } catch (Exception e) {
        }
    }
}
