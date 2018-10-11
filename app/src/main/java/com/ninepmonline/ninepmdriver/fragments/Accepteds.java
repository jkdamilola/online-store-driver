package com.ninepmonline.ninepmdriver.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ninepmonline.ninepmdriver.BaseContainerFragments;
import com.ninepmonline.ninepmdriver.NavigationActivity;
import com.ninepmonline.ninepmdriver.R;
import com.ninepmonline.ninepmdriver.WebServices.AppController;
import com.ninepmonline.ninepmdriver.WebServices.URL;
import com.ninepmonline.ninepmdriver.helper.AcceptesBeans;
import com.ninepmonline.ninepmdriver.helper.Constants;
import com.ninepmonline.ninepmdriver.helper.UpcommingsBean;
import com.ninepmonline.ninepmdriver.requests.CustomRequest;
import com.ninepmonline.ninepmdriver.requests.RequestCompleteListener;
import com.ninepmonline.ninepmdriver.requests.ViewLocations;
import com.ninepmonline.ninepmdriver.requests.VolleyRequest;
import com.ninepmonline.ninepmdriver.requests.products;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.widget.RelativeLayout.BELOW;

public class Accepteds extends BaseContainerFragments implements RequestCompleteListener<JSONArray> {
    static DisplayImageOptions selectsoptions;
    private Acceptesadap acceptsadapters;
    private ArrayList<AcceptesBeans> array_acceptedss;
    boolean cancel = false;
    private String completed_order_id = "";
    int completed_position;
    ArrayList<String> daddress;
    private Dialog dialog;
    ArrayList<String> dlatitude;
    private Gson gson;
    Acceptesadap.ViewHolders holders;
    private ImageView homes;
    ImageLoader imageLoader;
    private ListView listview;
    private boolean loadmore = false;
    ArrayList<String> logourl;
    private Databasehelper myDbHelper;
    private RelativeLayout noitemlay;
    int oi;
    int orders;
    private int pagenumbes;
    int positions;
    int positionss;
    private SharedPreferences preferences;
    private RelativeLayout progress_more;
    int reviewd_position;
    ArrayList<String> saddress;
    ArrayList<String> sh = new ArrayList();
    ArrayList<String> slatitude;
    ArrayList<String> sp = new ArrayList();
    private boolean state = false;
    ArrayList<String> storenamearray;
    TextView text_noitem = null;
    private int totalpage;
    ArrayList<String> usernamearray;

    class Acceptesadap extends BaseAdapter {
        LayoutInflater infls;
        Context scontext;

        class ViewHolders {
            TextView completestextview;
            TextView cusstextview;
            TextView customertextview;
            TextView datetextview;
            TextView destinationstextview;
            TextView distextview;
            ImageView image;
            TextView numberstextview;
            TextView orderstextview;
            TextView reviewtextviews;
            TextView sorurcetextview;
            TextView storetextview;
            TextView textview;

            ViewHolders() {
            }
        }

        public Acceptesadap(Context mcontext) {
            this.scontext = mcontext;
        }

        public int getCount() {
            return Accepteds.this.array_acceptedss.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(this.scontext, R.layout.upcoming_items, null);
                Accepteds.this.holders = new ViewHolders();
                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                SimpleDateFormat ss = new SimpleDateFormat("dd MMM yyyy hh:mm a");
                System.out.println("assssssssss" + Accepteds.this.array_acceptedss.size());
                holders.image = (ImageView) convertView.findViewById(R.id.imagestores);
                holders.storetextview = (TextView) convertView.findViewById(R.id.store_name);
                holders.datetextview = (TextView) convertView.findViewById(R.id.date);
                holders.orderstextview = (TextView) convertView.findViewById(R.id.ordersnumbers);
                holders.customertextview = (TextView) convertView.findViewById(R.id.textview_drivers_customers);
                holders.cusstextview = (TextView) convertView.findViewById(R.id.textview_drivers_customes);
                holders.numberstextview = (TextView) convertView.findViewById(R.id.textview_drivers_contacts);
                holders.distextview = (TextView) convertView.findViewById(R.id.textview_drivers_miles);
                holders.sorurcetextview = (TextView) convertView.findViewById(R.id.textview_drivers_sources);
                holders.destinationstextview = (TextView) convertView.findViewById(R.id.textview_drivers_destinations);
                holders.reviewtextviews = (TextView) convertView.findViewById(R.id.textview_drivers_reviews);
                holders.completestextview = (TextView) convertView.findViewById(R.id.textview_drivers_completes);
                holders.storetextview.setTypeface(Typeface.createFromAsset(Accepteds.this.getActivity().getAssets(), "PROXIMANOVA-SEMIBOLD.OTF"));
                holders.completestextview.setTypeface(Typeface.createFromAsset(Accepteds.this.getActivity().getAssets(), "PROXIMANOVA-SEMIBOLD.OTF"));
                holders.orderstextview.setTypeface(Typeface.createFromAsset(Accepteds.this.getActivity().getAssets(), "PROXIMANOVA-SEMIBOLD.OTF"));
                holders.cusstextview.setTypeface(Typeface.createFromAsset(Accepteds.this.getActivity().getAssets(), "PROXIMANOVA-REGULAR.OTF"));
                holders.reviewtextviews.setTypeface(Typeface.createFromAsset(Accepteds.this.getActivity().getAssets(), "PROXIMANOVA-REGULAR.OTF"));
                holders.customertextview.setTypeface(Typeface.createFromAsset(Accepteds.this.getActivity().getAssets(), "PROXIMANOVA-REGULAR.OTF"));
                holders.sorurcetextview.setTypeface(Typeface.createFromAsset(Accepteds.this.getActivity().getAssets(), "PROXIMANOVA-REGULAR.OTF"));
                holders.destinationstextview.setTypeface(Typeface.createFromAsset(Accepteds.this.getActivity().getAssets(), "PROXIMANOVA-REGULAR.OTF"));
                if (((AcceptesBeans) Accepteds.this.array_acceptedss.get(position)).getIs_complete().equals("1")) {
                    holders.completestextview.setText("Completed");
                    holders.reviewtextviews.setVisibility(View.GONE);
                } else {
                    holders.completestextview.setText("Completed?");
                    Accepteds.this.oi = Integer.parseInt(((AcceptesBeans) Accepteds.this.array_acceptedss.get(position)).getOrder_id());
                    holders.reviewtextviews.setVisibility(View.VISIBLE);
                    dlatitude.add(new StringBuilder(String.valueOf(((AcceptesBeans) Accepteds.this.array_acceptedss.get(position)).getShipping_lat())).append(" ,").append(((AcceptesBeans) Accepteds.this.array_acceptedss.get(position)).getShipping_long()).toString());
                }
                Accepteds.this.positions = position;
                Date ds = new Date();
                try {
                    ds = s.parse(((AcceptesBeans) Accepteds.this.array_acceptedss.get(position)).getCreated_at());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (((AcceptesBeans) Accepteds.this.array_acceptedss.get(position)).getIs_reviewed().equals("1")) {
                    holders.reviewtextviews.setText("Reviewed");
                } else {
                    holders.reviewtextviews.setText("Review Order");
                }
                holders.reviewtextviews.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((AcceptesBeans) Accepteds.this.array_acceptedss.get(position)).getIs_reviewed().equals("0")) {
                            Accepteds.this.oi = Integer.parseInt(((AcceptesBeans) Accepteds.this.array_acceptedss.get(position)).getOrder_id());
                            Accepteds.this.dialog = new Dialog(Accepteds.this.getActivity());
                            Accepteds.this.dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            Accepteds.this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                            Accepteds.this.dialog.setCancelable(true);
                            Accepteds.this.dialog.setContentView(R.layout.listdialogs);
                            ListView listview = (ListView) Accepteds.this.dialog.findViewById(R.id.listview_dialog);
                            TextView textviewdone = (TextView) Accepteds.this.dialog.findViewById(R.id.textviewdone);
                            TextView textviewlater = (TextView) Accepteds.this.dialog.findViewById(R.id.textviewlater);
                            textviewdone.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ArrayList<products> cheched_product = new ArrayList<products>();
                                    ArrayList<products> selected_product = new ArrayList<products>();
                                    Cursor cursur_cp = Accepteds.this.myDbHelper.getAllProducts();
                                    if (cursur_cp.moveToFirst()) {
                                        do {
                                            String data = cursur_cp.getString(cursur_cp.getColumnIndex("order_id"));
                                            Accepteds.this.orders = Integer.parseInt(data);
                                            cheched_product.add(new products(cursur_cp.getString(cursur_cp.getColumnIndex("product_id")), "", "", "", "", "", cursur_cp.getString(cursur_cp.getColumnIndex("order_id")), true));
                                        } while (cursur_cp.moveToNext());
                                    }
                                    cursur_cp.close();
                                    for (int zz = 0; zz < cheched_product.size(); zz++) {
                                        if (Accepteds.this.oi == Integer.parseInt(((products) cheched_product.get(zz)).getorderi())) {
                                            String pid = ((products) cheched_product.get(zz)).getProduct_id();
                                            for (int i = 0; i < ((AcceptesBeans) Accepteds.this.array_acceptedss.get(i)).getProducts().size(); i++) {
                                                if (((products) ((AcceptesBeans) Accepteds.this.array_acceptedss.get(i)).getProducts().get(i)).getProduct_id().equals(pid)) {
                                                    selected_product.add((products) ((AcceptesBeans) Accepteds.this.array_acceptedss.get(i)).getProducts().get(i));
                                                }
                                            }
                                        }
                                    }
                                    Log.v("LogView", "Selected List Length   :" + selected_product.size());
                                    Accepteds.this.confrimReviewOrder(position, Accepteds.this.oi, selected_product);
                                }
                            });
                            textviewlater.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Accepteds.this.dialog.dismiss();
                                }
                            });
                            listview.setAdapter(new listAdapter(Acceptesadap.this.scontext, ((AcceptesBeans) Accepteds.this.array_acceptedss.get(position)).getProducts()));
                            Accepteds.this.dialog.show();
                            return;
                        }
                        Accepteds.this.oneButtonAlert("Sorry", "Reviewed order can not be edit", "Ok");
                    }
                });
                holders.completestextview.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Constants.isInternetOn(Accepteds.this.getActivity())) {
                            final Dialog C_dialog = new Dialog(Accepteds.this.getActivity());
                            C_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            C_dialog.setCancelable(false);
                            C_dialog.setCanceledOnTouchOutside(false);
                            C_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                            View t = LayoutInflater.from(Accepteds.this.getActivity()).inflate(R.layout.logsout, null);
                            C_dialog.setContentView(R.layout.logsout);
                            TextView textview = (TextView) C_dialog.findViewById(R.id.textview__header);
                            TextView textview_messages = (TextView) C_dialog.findViewById(R.id.textview__messages);
                            textview.setText("Confirmation");
                            textview_messages.setText("Are you sure you want to mark this as completed?");
                            Button submit = (Button) C_dialog.findViewById(R.id.button_ok);
                            Button cancel = (Button) C_dialog.findViewById(R.id.button_cancel);
                            submit.setText("Yes");
                            cancel.setText("No");
                            submit.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (Constants.isInternetOn(Accepteds.this.getActivity())) {
                                        Accepteds.this.oi = Integer.parseInt(((AcceptesBeans) Accepteds.this.array_acceptedss.get(position)).getOrder_id());
                                        Accepteds.this.completed_order_id = ((AcceptesBeans) Accepteds.this.array_acceptedss.get(position)).getOrder_id();
                                        Accepteds.this.completed_position = position;
                                        Accepteds.this.positionss = position;
                                        for (int z = 0; z < Accepteds.this.array_acceptedss.size(); z++) {
                                            if (z == position && ((AcceptesBeans) Accepteds.this.array_acceptedss.get(position)).getIs_complete().equals("0")) {
                                                Accepteds.this.Compeletes();
                                                ((AcceptesBeans) Accepteds.this.array_acceptedss.get(position)).setIs_complete("1");
                                            }
                                        }
                                        C_dialog.dismiss();
                                        Acceptesadap.this.notifyDataSetChanged();
                                        return;
                                    }
                                    Constants.errordialogs("Alert", "Please Connect to Internet", Accepteds.this.getActivity());
                                }
                            });
                            cancel.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    C_dialog.dismiss();
                                }
                            });
                            textview.setTypeface(Typeface.createFromAsset(Accepteds.this.getActivity().getAssets(), "PROXIMANOVA-REGULAR.OTF"));
                            textview_messages.setTypeface(Typeface.createFromAsset(Accepteds.this.getActivity().getAssets(), "PROXIMANOVA-REGULAR.OTF"));
                            submit.setTypeface(Typeface.createFromAsset(Accepteds.this.getActivity().getAssets(), "PROXIMANOVA-REGULAR.OTF"));
                            cancel.setTypeface(Typeface.createFromAsset(Accepteds.this.getActivity().getAssets(), "PROXIMANOVA-REGULAR.OTF"));
                            C_dialog.show();
                        } else {
                            Constants.errordialogs("Alert", "Please Connect to Internet", Accepteds.this.getActivity());
                        }
                    }
                });
                holders.storetextview.setText(((AcceptesBeans) Accepteds.this.array_acceptedss.get(position)).getStore_name());
                holders.datetextview.setText(ss.format(ds));
                holders.orderstextview.setText("Order No. #" + ((AcceptesBeans) Accepteds.this.array_acceptedss.get(position)).getOrder_id());
                holders.cusstextview.setText(((AcceptesBeans) Accepteds.this.array_acceptedss.get(position)).getCustomer_name());
                holders.numberstextview.setText(((AcceptesBeans) Accepteds.this.array_acceptedss.get(position)).getTelephone());
                holders.sorurcetextview.setText(((AcceptesBeans) Accepteds.this.array_acceptedss.get(position)).getStore_address());
                Accepteds.this.imageLoader.displayImage(((AcceptesBeans) Accepteds.this.array_acceptedss.get(position)).getLogo_url(), holders.image, Accepteds.selectsoptions);
                if (((AcceptesBeans) Accepteds.this.array_acceptedss.get(position)).getShipping_address_1().equals(null)) {
                    holders.destinationstextview.setText(new StringBuilder(String.valueOf(((AcceptesBeans) Accepteds.this.array_acceptedss.get(position)).getShipping_address_1())).append("\n ").append(((AcceptesBeans) Accepteds.this.array_acceptedss.get(position)).getShipping_city()).append(", ").append(((AcceptesBeans) Accepteds.this.array_acceptedss.get(position)).getShipping_postcode()).toString());
                } else if (((AcceptesBeans) Accepteds.this.array_acceptedss.get(position)).getShipping_address_2().equals(null)) {
                    holders.destinationstextview.setText(new StringBuilder(String.valueOf(((AcceptesBeans) Accepteds.this.array_acceptedss.get(position)).getShipping_address_1())).append("\n ").append(((AcceptesBeans) Accepteds.this.array_acceptedss.get(position)).getShipping_city()).append(", ").append(((AcceptesBeans) Accepteds.this.array_acceptedss.get(position)).getShipping_postcode()).toString());
                } else if (((AcceptesBeans) Accepteds.this.array_acceptedss.get(position)).getShipping_city().equals(null)) {
                    holders.destinationstextview.setText(new StringBuilder(String.valueOf(((AcceptesBeans) Accepteds.this.array_acceptedss.get(position)).getShipping_address_1())).append("\n ").append(((AcceptesBeans) Accepteds.this.array_acceptedss.get(position)).getShipping_city()).append(", ").append(((AcceptesBeans) Accepteds.this.array_acceptedss.get(position)).getShipping_postcode()).toString());
                } else {
                    holders.destinationstextview.setText(new StringBuilder(String.valueOf(((AcceptesBeans) Accepteds.this.array_acceptedss.get(position)).getShipping_address_1())).append("\n ").append(((AcceptesBeans) Accepteds.this.array_acceptedss.get(position)).getShipping_city()).append(", ").append(((AcceptesBeans) Accepteds.this.array_acceptedss.get(position)).getShipping_postcode()).toString());
                }
                try {
                    String distances = String.valueOf(new Float((float) Math.round(Accepteds.this.CalculationByDistance(new LatLng(Double.valueOf(((AcceptesBeans) Accepteds.this.array_acceptedss.get(Accepteds.this.positions)).getStore_lat()).doubleValue(), Double.valueOf(((AcceptesBeans) Accepteds.this.array_acceptedss.get(Accepteds.this.positions)).getStore_long()).doubleValue()), new LatLng(Double.valueOf(((AcceptesBeans) Accepteds.this.array_acceptedss.get(Accepteds.this.positions)).getShipping_lat()).doubleValue(), Double.valueOf(((AcceptesBeans) Accepteds.this.array_acceptedss.get(Accepteds.this.positions)).getShipping_long()).doubleValue())))));
                    Log.e("asssssssssss", ((AcceptesBeans) Accepteds.this.array_acceptedss.get(Accepteds.this.positions)).getIs_complete());
                    holders.distextview.setText(new StringBuilder(String.valueOf(distances)).append(" ").append(" miles").toString());
                } catch (Exception e2) {
                    holders.distextview.setText("0.0 miles");
                }
                convertView.setTag(holders);
            } else {
                holders = (ViewHolders) convertView.getTag();
            }
            return convertView;
        }
    }

    private static class SelectViewHolder {
        private CheckBox checkBox;
        RelativeLayout ls;
        ImageView storeimage;
        private TextView storename;
        private TextView totals;

        public SelectViewHolder(TextView storename, CheckBox checkBox, ImageView storeimage, RelativeLayout ls, TextView totals) {
            this.checkBox = checkBox;
            this.storename = storename;
            this.storeimage = storeimage;
            this.ls = ls;
            this.totals = totals;
        }

        public CheckBox getCheckBox() {
            return this.checkBox;
        }

        public void setCheckBox(CheckBox checkBox) {
            this.checkBox = checkBox;
        }

        public TextView getstorename() {
            return this.storename;
        }

        public void setstorename(TextView storename) {
            this.storename = storename;
        }

        public void setStoreimage(ImageView storeimage) {
            this.storeimage = storeimage;
        }

        public void setLs(RelativeLayout ls) {
            this.ls = ls;
        }

        public void setTotals(TextView totals) {
            this.totals = totals;
        }

        public RelativeLayout getLs() {
            return this.ls;
        }

        public ImageView getStoreimage() {
            return this.storeimage;
        }

        public TextView getStorename() {
            return this.storename;
        }

        public TextView getTotals() {
            return this.totals;
        }
    }

    private class listAdapter extends ArrayAdapter<products> {
        Cursor cursur_all = null;
        private LayoutInflater inflater;
        List<products> planets = new ArrayList<products>();

        public listAdapter(Context context, List<products> planetList) {
            super(context, R.layout.listsdialog, planetList);
            this.planets = planetList;
            this.inflater = LayoutInflater.from(context);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            TextView storename;
            CheckBox checkBox;
            ImageView storeimage;
            TextView totals;
            products planet = (products) getItem(position);
            this.cursur_all = Accepteds.this.myDbHelper.getAllProducts();
            ArrayList<products> productss = new ArrayList<products>();
            if (this.cursur_all.moveToFirst()) {
                do {
                    String data = this.cursur_all.getString(this.cursur_all.getColumnIndex("order_id"));
                    Accepteds.this.orders = Integer.parseInt(data);
                    System.out.println("assssssssssssssssssssss" + this.cursur_all.getCount() + "            " + Accepteds.this.orders);
                    productss.add(new products(this.cursur_all.getString(this.cursur_all.getColumnIndex("product_id")), "", "", "", "", "", this.cursur_all.getString(this.cursur_all.getColumnIndex("order_id")), true));
                } while (this.cursur_all.moveToNext());
            }
            this.cursur_all.close();
            if (convertView == null) {
                convertView = this.inflater.inflate(R.layout.listsdialog, null);
                storename = (TextView) convertView.findViewById(R.id.storename);
                checkBox = (CheckBox) convertView.findViewById(R.id.rls);
                storeimage = (ImageView) convertView.findViewById(R.id.image);
                totals = (TextView) convertView.findViewById(R.id.textview_total);
                View view = convertView;
                view.setTag(new SelectViewHolder(storename, checkBox, storeimage, (RelativeLayout) convertView.findViewById(R.id.rst), totals));
                checkBox.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        products planet = (products) cb.getTag();
                        planet.setIsselect(cb.isChecked());
                        if (cb.isChecked()) {
                            Accepteds.this.myDbHelper.addStore((AcceptesBeans) Accepteds.this.array_acceptedss.get(Accepteds.this.positions), planet.getProduct_id(), Accepteds.this.oi);
                        } else if (Accepteds.this.oi == Integer.parseInt(planet.getorderi())) {
                            int b = Accepteds.this.myDbHelper.deleteProductByid(planet.getProduct_id(), planet.getorderi());
                            System.out.println("ssssssssssss" + planet.getProduct_id() + " " + planet.getorderi());
                        }
                    }
                });
            } else {
                SelectViewHolder viewHolder = (SelectViewHolder) convertView.getTag();
                checkBox = viewHolder.getCheckBox();
                storename = viewHolder.getstorename();
                storeimage = viewHolder.getStoreimage();
                RelativeLayout ls = viewHolder.getLs();
                totals = viewHolder.getTotals();
            }
            for (int zz = 0; zz < this.cursur_all.getCount(); zz++) {
                if (Accepteds.this.oi == Integer.parseInt(((products) productss.get(zz)).getorderi())) {
                    if (planet.getProduct_id().equals(((products) productss.get(zz)).getProduct_id())) {
                        planet.setIsselect(((products) productss.get(zz)).getIsselect());
                        checkBox.setChecked(true);
                    } else {
                        checkBox.setChecked(false);
                    }
                }
                notifyDataSetChanged();
            }
            checkBox.setTag(planet);
            checkBox.setChecked(planet.getIsselect());
            storename.setText(planet.getName());
            Accepteds.this.imageLoader.displayImage(((products) this.planets.get(position)).getImage(), storeimage, Accepteds.selectsoptions);
            String price = String.format("%.2f", new Object[]{Double.valueOf(planet.getPrice())});
            Double quantity = Double.valueOf(planet.getQuantity());
            totals.setText(quantity + " X" + price + " = " + String.format("%.2f", new Object[]{Double.valueOf(planet.getTotal())}));
            return convertView;
        }
    }

    public void onTaskComplete(String tag, JSONArray response) {
        JSONObject obj = new JSONObject();
        String driverid = this.preferences.getString(Constants.driverid, "");
        try {
            obj.put("result", response);
            Log.v("LogView", "ACCEPTED List Res : " + tag + " " + response.toString());
            if (response.length() > 0) {
                int z;
                if (tag.equals("ACCEPTED")) {
                    JSONArray items = obj.getJSONArray("result");
                    if (items.length() == 0) {
                        this.progress_more.setVisibility(View.GONE);
                        if (this.array_acceptedss.size() == 0) {
                            this.noitemlay.setVisibility(View.VISIBLE);
                            return;
                        }
                        return;
                    }
                    if (!this.loadmore) {
                        this.loadmore = true;
                        this.totalpage += 1;
                    }
                    this.state = true;
                    this.noitemlay.setVisibility(View.GONE);
                    for (z = 0; z < items.length(); z++) {
                        Log.v("LogView", "ACCEPTED List Res : " + tag + " " + items.toString());
                        JSONObject os = items.getJSONObject(z);
                        String order_id = os.getString("id");
                        JSONObject shipping = os.getJSONObject("shipping");
                        JSONObject billing = os.getJSONObject("billing");
                        String customer_name = shipping.getString("last_name") + " " + shipping.getString("first_name");
                        String store_address = "9PM Retailers Ltd, Abuja";
                        String store_lat = "0.0";
                        String store_long = "0.0";
                        String logo_url = "";
                        String total = os.getString("total");
                        String store_name = "9pm Retailers Ltd";
                        String status = os.getString("status");
                        String telephone = billing.getString("phone");
                        String shipping_firstname = shipping.getString("first_name");
                        String shipping_lastname = shipping.getString("last_name");
                        String shipping_address_1 = shipping.getString("address_1");
                        String shipping_address_2 = shipping.getString("address_2");
                        String shipping_city = shipping.getString("city");
                        String shipping_postcode = shipping.getString("postcode");
                        String assign_count = "" + os.getJSONArray("line_items").length();
                        String product_count = "" + os.getJSONArray("line_items").length();
                        String created_at = os.getString("date_created");
                        String shipping_lat = "null";
                        String shipping_long = "null";
                        if (shipping_lat.equals("null")) {
                            shipping_lat = "0.0";
                        }
                        if (shipping_long.equals("null")) {
                            shipping_long = "0.0";
                        }
                        JSONArray products = os.getJSONArray("line_items");
                        ArrayList<products> arrayproducts = new ArrayList<products>();
                        for (int zz = 0; zz < products.length(); zz++) {
                            JSONObject ss = products.getJSONObject(zz);
                            arrayproducts.add(new products(ss.getString("product_id"), ss.getString("name"), ss.getString("quantity"), ss.getString("price"), ss.getString("total"), "", order_id, false));
                        }
                        String is_complete = "0";
                        if (!is_complete.equals("yes")) {
                            this.slatitude.add(new StringBuilder(String.valueOf(store_lat)).append(" ,").append(store_long).toString());
                            this.dlatitude.add(new StringBuilder(String.valueOf(shipping_lat)).append(" ,").append(shipping_long).toString());
                            this.daddress.add(new StringBuilder(String.valueOf(shipping_address_1)).append("\n").append(shipping_city).toString());
                            this.saddress.add(store_address);
                            this.logourl.add(logo_url);
                            this.storenamearray.add(store_name);
                            this.usernamearray.add(customer_name);
                        }

                        JSONArray metas = os.getJSONArray("meta_data");
                        for (int m = 0; m < metas.length(); m++) {
                            JSONObject meta = metas.getJSONObject(m);
                            if (meta.getString("key").equals("_deliveryman_id") && meta.getString("value").equals(driverid)) {
                                this.array_acceptedss.add(new AcceptesBeans(order_id, customer_name, store_address, store_lat, store_long, logo_url, total, store_name, status, telephone, shipping_firstname, shipping_lastname, shipping_address_1, shipping_address_2, shipping_city, shipping_postcode, product_count, created_at, shipping_lat, shipping_long, arrayproducts, is_complete, "0", "0"));
                            }
                        }
                    }
                    if (array_acceptedss.size() == 0) {
                        this.noitemlay.setVisibility(View.VISIBLE);
                        return;
                    }
                    Log.v("Array Accepted", "Size is " + array_acceptedss.size());
                    for (int i = 0; i < this.array_acceptedss.size(); i++) {
                        if (this.myDbHelper.getViewed(Integer.parseInt(((AcceptesBeans) this.array_acceptedss.get(i)).getOrder_id())).getCount() > 0) {
                            ((AcceptesBeans) this.array_acceptedss.get(i)).setIs_completes("1");
                        }
                    }
                    this.acceptsadapters.notifyDataSetChanged();
                    this.progress_more.setVisibility(View.GONE);
                }
                if (tag.equals("REVIEW_ORDER")) {
                    this.dialog.dismiss();
                    if (!this.cancel) {
                        for (z = 0; z < this.array_acceptedss.size(); z++) {
                            if (z == this.reviewd_position) {
                                if (this.oi != Integer.parseInt(((AcceptesBeans) this.array_acceptedss.get(this.reviewd_position)).getOrder_id())) {
                                    ((AcceptesBeans) this.array_acceptedss.get(z)).setIs_completes(((AcceptesBeans) this.array_acceptedss.get(this.reviewd_position)).getIs_completes());
                                    ((AcceptesBeans) this.array_acceptedss.get(z)).setIs_reviewed(((AcceptesBeans) this.array_acceptedss.get(this.reviewd_position)).getIs_reviewed());
                                } else if (((AcceptesBeans) this.array_acceptedss.get(z)).getIs_completes().equals("0")) {
                                    boolean zz2 = this.myDbHelper.ComStore("1", this.oi);
                                    holders.reviewtextviews.setText("Reviewed");
                                    ((AcceptesBeans) this.array_acceptedss.get(z)).setIs_completes("1");
                                    ((AcceptesBeans) this.array_acceptedss.get(z)).setIs_reviewed("1");
                                    System.out.println("CompleteComplete");
                                } else {
                                    ((AcceptesBeans) this.array_acceptedss.get(z)).setIs_completes(((AcceptesBeans) this.array_acceptedss.get(this.reviewd_position)).getIs_completes());
                                    ((AcceptesBeans) this.array_acceptedss.get(z)).setIs_reviewed(((AcceptesBeans) this.array_acceptedss.get(this.reviewd_position)).getIs_reviewed());
                                }
                            }
                            this.acceptsadapters.notifyDataSetChanged();
                        }
                        return;
                    }
                }
                return;
            }
            if (this.array_acceptedss.size() == 0) {
                this.noitemlay.setVisibility(View.VISIBLE);
            }
           // Constants.errordialogs("Alert", response.getJSONArray("errors").getJSONObject(0).getString("message"), getActivity());
        } catch (Exception e) {
            if (this.array_acceptedss.size() == 0) {
                this.noitemlay.setVisibility(View.VISIBLE);
            }
            this.progress_more.setVisibility(View.GONE);
            Log.i("error ", e.toString());
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        this.pagenumbes = 1;
        this.totalpage = 1;
        View rootview = inflater.inflate(R.layout.upcoming_listing, null);
        this.preferences = getActivity().getSharedPreferences(Constants.INSTAFRESH, 0);
        selectsoptions = new Builder().showImageOnFail((int) R.drawable.default_store_icon).showImageOnLoading((int) R.drawable.default_store_icon).showImageForEmptyUri((int) R.drawable.default_store_icon).cacheInMemory(true).considerExifParams(true).cacheOnDisc(true).displayer(new RoundedBitmapDisplayer(20, 10)).build();
        this.imageLoader = ImageLoader.getInstance();
        this.imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        String logindatass = this.preferences.getString(Constants.LOGIN, null);
        try {
            Type testType = new TypeToken<JSONObject>() {
            }.getType();
            String logindatas = this.preferences.getString(Constants.LOGIN, "");
            if (logindatas != null && logindatas.length() > 0) {
                JSONObject js = (JSONObject) this.gson.fromJson(logindatas, testType);
                System.out.println("asssssssss" + logindatas);
            }
        } catch (Exception e) {
            Log.e("assssssss", e.toString());
        }
        Log.e("assssssss", logindatass);
        this.myDbHelper = new Databasehelper(getActivity());
        try {
            this.myDbHelper.createDatabase();
            this.myDbHelper.openDatabase();
            this.slatitude = new ArrayList();
            this.dlatitude = new ArrayList();
            this.saddress = new ArrayList();
            this.daddress = new ArrayList();
            this.logourl = new ArrayList();
            this.storenamearray = new ArrayList();
            this.usernamearray = new ArrayList();
            this.homes = (ImageView) rootview.findViewById(R.id.homes);
            this.listview = (ListView) rootview.findViewById(R.id.listupcoming);
            this.progress_more = (RelativeLayout) rootview.findViewById(R.id.progressbas);
            ((ImageView) rootview.findViewById(R.id.editicon)).setVisibility(View.INVISIBLE);
            this.text_noitem = (TextView) rootview.findViewById(R.id.text_noitem);
            this.text_noitem.setText("All accepted delivery listed here");
            TextView textview = (TextView) rootview.findViewById(R.id.appname);
            textview.setText("Accepted Delivery");
            this.noitemlay = (RelativeLayout) rootview.findViewById(R.id.noitemlay);
            RelativeLayout selects = (RelativeLayout) rootview.findViewById(R.id.headers);
            RelativeLayout framelayouts = (RelativeLayout) rootview.findViewById(R.id.linearslayout);
            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                getActivity().getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            }
            int usableHeighs = metrics.heightPixels;
            LayoutParams p = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, usableHeighs);
            p.addRule(BELOW, selects.getId());
            textview.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "PROXIMANOVA-SEMIBOLD.OTF"));
            framelayouts.setLayoutParams(p);
            // Log.i("ttheight", (usableHeighs - usableHeight));
            DisplayMetrics metricss = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableHeights = metrics.heightPixels;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                getActivity().getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            }
            int usableHeighss = usableHeight - metrics.heightPixels;
            new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT).setMargins(0, 0, 0, usableHeighs);
            this.array_acceptedss = new ArrayList();
            if (Constants.isInternetOn(getActivity())) {
                getaccepteds();
            } else {
                Constants.errordialogs("Alert", "Please Connect to Internet", getActivity());
            }
            this.acceptsadapters = new Acceptesadap(getActivity());
            this.listview.setAdapter(this.acceptsadapters);
            System.out.println("assssssssssss" + this.array_acceptedss.size());
            initview();
            this.listview.setOnScrollListener(new OnScrollListener() {
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    int count = Accepteds.this.listview.getCount();
                    System.out.println("threshold" + count);
                    if (scrollState == 0 && Accepteds.this.listview.getLastVisiblePosition() >= count - 1) {
                        System.out.println("on scrollll" + Accepteds.this.state);
                        if (!Accepteds.this.state) {
                            return;
                        }
                        if (Constants.isInternetOn(Accepteds.this.getActivity())) {
                            Accepteds.this.state = false;
                            Accepteds.this.loadmore();
                            // Log.e("assssssss", Accepteds.this.array_acceptedss);
                            return;
                        }
                        Constants.errordialogs("Alert", "Please Connect to Internet", Accepteds.this.getActivity());
                    }
                }

                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                }
            });
            navigates();
            onlisteners();
            LinearLayout viewlocations = (LinearLayout) rootview.findViewById(R.id.linearlayout_drivers);
            viewlocations.setVisibility(View.GONE);
            viewlocations.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Accepteds.this.getActivity(), ViewLocations.class);
                    intent.putStringArrayListExtra("slist", Accepteds.this.slatitude);
                    intent.putStringArrayListExtra("dlist", Accepteds.this.dlatitude);
                    intent.putStringArrayListExtra("saddress", Accepteds.this.saddress);
                    intent.putStringArrayListExtra("daddress", Accepteds.this.daddress);
                    intent.putStringArrayListExtra("logourl", Accepteds.this.logourl);
                    intent.putStringArrayListExtra("storename", Accepteds.this.storenamearray);
                    intent.putStringArrayListExtra("username", Accepteds.this.usernamearray);
                    Accepteds.this.getActivity().startActivity(intent);
                }
            });
            return rootview;
        } catch (IOException e2) {
            throw new Error("Unable to create database");
        }
    }

    void onlisteners() {
    }

    void getaccepteds() {
        if (this.totalpage == 1) {
            new VolleyRequest(getActivity(), this, null).makeArrayRequest(Request.Method.GET, URL.GETACCEPTEDS.getUrl(), null, null, "", "ACCEPTED", true);
            return;
        }
        new VolleyRequest(getActivity(), this, null).makeArrayRequest(Request.Method.GET, new StringBuilder(String.valueOf(URL.GETACCEPTEDS.getUrl())).append("&page=").append(this.totalpage).toString(), null, null, "", "ACCEPTED", false);
    }

    void initview() {
    }

    void navigates() {
        this.homes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationActivity navigationActivity = new NavigationActivity();
                navigationActivity.resideMenu.openMenu(0);
            }
        });
    }

    void Compeletes() {
        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        Map<String, String> params = new HashMap<String, String>();
        params.put("status", "completed");

        String url = String.format(URL.ACCEPTORDERS.getUrl(), this.oi);
        CustomRequest objectRequest = new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                pDialog.hide();
                if (((AcceptesBeans) array_acceptedss.get(completed_position)).getOrder_id().equals(completed_order_id)) {
                    array_acceptedss.remove(completed_position);
                }
                acceptsadapters.notifyDataSetChanged();
                if (array_acceptedss.size() == 0) {
                    noitemlay.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();
            }
        });
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(60000, 1, 1.0f));
        AppController.getInstance().addToRequestQueue(objectRequest, "REQUEST0");
    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double c = 2.0d * Math.asin(Math.sqrt((Math.sin(dLat / 2.0d) * Math.sin(dLat / 2.0d)) + (((Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))) * Math.sin(dLon / 2.0d)) * Math.sin(dLon / 2.0d))));
        double valueResult = ((double) 6371) * c;
        double km = valueResult / 1.0d;
        DecimalFormat decimalFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(decimalFormat.format(km)).intValue();
        Log.i("Radius Value", valueResult + "   KM  " + kmInDec + " Meter   " + Integer.valueOf(decimalFormat.format(valueResult % 1000.0d)).intValue());
        return ((double) 6371) * c;
    }

    private void confrimReviewOrder(final int position, int order_id, ArrayList<products> selected_product) {
        if (Constants.isInternetOn(getActivity())) {
            final Dialog slid_dialog = new Dialog(getActivity());
            slid_dialog.requestWindowFeature(1);
            slid_dialog.setCancelable(false);
            slid_dialog.setCanceledOnTouchOutside(false);
            slid_dialog.setContentView(R.layout.submit_review_dialog);
            slid_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            // slid_dialog.getWindow().getAttributes().windowAnimations = R.style.Animations.SmileWindow;
            TextView tv_msg = (TextView) slid_dialog.findViewById(R.id.tv_msg);
            TextView tv_confirm = (TextView) slid_dialog.findViewById(R.id.tv_confirm);
            Button btn_later = (Button) slid_dialog.findViewById(R.id.btn_later);
            btn_later.setTransformationMethod(null);
            if (selected_product.size() == 0) {
                tv_msg.setText("You haven't selected/picked any product from store, If you continue order will be cancelled automatically. Sure you want to continue?");
            } else {
                tv_msg.setText("By selecting this option, you're confirming that marked items are collected from store and customer will be charged accordingly.");
            }
            final ArrayList<products> arrayList = selected_product;
            final int i2 = order_id;
            tv_confirm.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    slid_dialog.dismiss();
                    if (Constants.isInternetOn(Accepteds.this.getActivity())) {
                        if (arrayList.size() == 0) {
                            Accepteds.this.cancel = true;
                            Accepteds.this.array_acceptedss.remove(position);
                            Accepteds.this.acceptsadapters.notifyDataSetChanged();
                        } else {
                            Accepteds.this.cancel = false;
                        }
                        if (Accepteds.this.array_acceptedss.size() == 0) {
                            Accepteds.this.noitemlay.setVisibility(View.VISIBLE);
                        }
                        Accepteds.this.reviewOrderRequest(i2, arrayList);
                        Accepteds.this.reviewd_position = position;
                        return;
                    }
                    Constants.errordialogs("Alert", "Please Connect to Internet", Accepteds.this.getActivity());
                }
            });
            btn_later.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    slid_dialog.dismiss();
                    Accepteds.this.dialog.dismiss();
                }
            });
            slid_dialog.show();
            return;
        }
        Constants.errordialogs("Alert", "Please Connect to Internet", getActivity());
    }

    private void oneButtonAlert(String header, String msg, String btn_text) {
        final Dialog sorry_alert = new Dialog(getActivity());
        sorry_alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sorry_alert.setCancelable(false);
        sorry_alert.setCanceledOnTouchOutside(false);
        sorry_alert.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        sorry_alert.setContentView(R.layout.alert_popup);
        TextView tv_error_header = (TextView) sorry_alert.findViewById(R.id.tv_error_header);
        TextView tv_error_message = (TextView) sorry_alert.findViewById(R.id.tv_error_message);
        Button button_ok = (Button) sorry_alert.findViewById(R.id.button_ok);
        tv_error_header.setText(header);
        tv_error_message.setText(msg);
        button_ok.setText(btn_text);
        button_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sorry_alert.dismiss();
            }
        });
        tv_error_header.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "PROXIMANOVA-REGULAR.OTF"));
        tv_error_message.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "PROXIMANOVA-REGULAR.OTF"));
        button_ok.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "PROXIMANOVA-REGULAR.OTF"));
        sorry_alert.show();
    }

    private void reviewOrderRequest(int review_order_id, ArrayList<products> selected_product) {
        Log.v("LogView", "Selected List Length  222 :" + selected_product.size());
        JSONObject main_Params = new JSONObject();
        try {
            JSONArray ja = new JSONArray();
            for (int i = 0; i < selected_product.size(); i++) {
                JSONObject jo = new JSONObject();
                ja.put(((products) selected_product.get(i)).getProduct_id());
                jo.put("total", ((products) selected_product.get(i)).getTotal());
                jo.put("image", ((products) selected_product.get(i)).getImage());
                jo.put("quantity", ((products) selected_product.get(i)).getQuantity());
                jo.put("price", ((products) selected_product.get(i)).getPrice());
                jo.put("product_id", ((products) selected_product.get(i)).getProduct_id());
                jo.put("name", ((products) selected_product.get(i)).getName());
            }
            main_Params.put("products", ja);
            main_Params.put("order_id", review_order_id);
            main_Params.put("access_token", this.preferences.getString(Constants.ACCESS_TOKEN, ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("LogView", "REVIEW_ORDER Sending Request :" + main_Params.toString());
        new VolleyRequest(getActivity(), this, null).makeRequest(Request.Method.POST, URL.REVIEW_ORDER.getUrl(), null, null, "", "REVIEW_ORDER", true);
    }

    void loadmore() {
        this.progress_more.setVisibility(View.VISIBLE);
        this.pagenumbes++;
        if (this.pagenumbes > this.totalpage) {
            this.progress_more.setVisibility(View.GONE);
            Constants.showMessage("No More", getActivity());
            return;
        }
        System.out.println("assssssssss" + this.totalpage);
        if (Constants.isInternetOn(getActivity())) {
            getaccepteds();
        } else {
            Constants.errordialogs("Alert", "Please Connect to Internet", getActivity());
        }
    }
}
