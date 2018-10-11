package com.ninepmonline.ninepmdriver.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ninepmonline.ninepmdriver.BaseContainerFragments;
import com.ninepmonline.ninepmdriver.NavigationActivity;
import com.ninepmonline.ninepmdriver.R;
import com.ninepmonline.ninepmdriver.WebServices.AppController;
import com.ninepmonline.ninepmdriver.WebServices.URL;
import com.ninepmonline.ninepmdriver.helper.Constants;
import com.ninepmonline.ninepmdriver.helper.UpcommingsBean;
import com.ninepmonline.ninepmdriver.requests.CustomRequest;
import com.ninepmonline.ninepmdriver.requests.RequestCompleteListener;
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

public class Upcomings extends BaseContainerFragments implements RequestCompleteListener<JSONArray> {
    int acceptespos = 0;
    ArrayList<UpcommingsBean> array_upcomings;
    boolean cancel = false;
    private String completed_order_id = "";
    int completed_position;
    private Dialog dialog;
    String driverid = "";
    Gson gson;
    ImageView homes;
    ImageLoader imageLoader;

    OnClickListener listeners = new OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.homes:
                    NavigationActivity.resideMenu.openMenu(0);
                    return;
                default:
                    return;
            }
        }
    };
    ListView listview;
    private boolean loadmore = false;
    private Databasehelper myDbHelper;
    private RelativeLayout noitemlay;
    int oi;
    String orderid;
    int orders;
    private int pagenumber = 1;
    int positions;
    int positionss;
    private SharedPreferences preferences;
    private RelativeLayout progress_more;
    int reviewd_position;
    DisplayImageOptions selectsoptions;
    private boolean state = false;
    private int totalpage = 0;
    UpcommingsAdapters upcommingsadapters;

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

    class UpcommingsAdapters extends BaseAdapter {
        Context scontext;

        public UpcommingsAdapters(Context mcontext) {
            this.scontext = mcontext;
        }

        public int getCount() {
            return Upcomings.this.array_upcomings.size();
        }

        public Object getItem(int position) {
            return Integer.valueOf(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(final int position, View convertView, final ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(Upcomings.this.getActivity(), R.layout.upcoming_items, null);
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm a");
            Date ds = new Date();
            try {
                ds = simpleDateFormat.parse(((UpcommingsBean) Upcomings.this.array_upcomings.get(position)).getCreated_at());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ImageView storeimage = (ImageView) convertView.findViewById(R.id.imagestores);
            TextView storesTextview = (TextView) convertView.findViewById(R.id.store_name);
            TextView reviewtextviews = (TextView) convertView.findViewById(R.id.textview_drivers_reviews);
            TextView datetextview = (TextView) convertView.findViewById(R.id.date);
            TextView orderstextview = (TextView) convertView.findViewById(R.id.ordersnumbers);
            ImageView ticksimageview = (ImageView) convertView.findViewById(R.id.imageview_drivers_corrects);
            ticksimageview.setVisibility(View.VISIBLE);
            ImageView crossimageview = (ImageView) convertView.findViewById(R.id.imageview_drivers_cross);
            crossimageview.setVisibility(View.INVISIBLE);
            TextView reviewstextview = (TextView) convertView.findViewById(R.id.textview_drivers_reviews);
            TextView xxmilestext = (TextView) convertView.findViewById(R.id.textview_drivers_miles);
            datetextview.setTypeface(Typeface.createFromAsset(Upcomings.this.getActivity().getAssets(), "PROXIMANOVA-REGULAR.OTF"));
            orderstextview.setTypeface(Typeface.createFromAsset(Upcomings.this.getActivity().getAssets(), "PROXIMANOVA-REGULAR.OTF"));
            reviewtextviews.setVisibility(View.VISIBLE);
            Upcomings.this.imageLoader.displayImage(((UpcommingsBean) Upcomings.this.array_upcomings.get(position)).getLogo_url(), storeimage, Upcomings.this.selectsoptions);
            String distance = "";
            try {
                xxmilestext.setText(new StringBuilder(String.valueOf(String.valueOf(new Float((float) Math.round(Upcomings.this.CalculationByDistance(new LatLng(Double.valueOf(((UpcommingsBean) Upcomings.this.array_upcomings.get(position)).getStore_lat()).doubleValue(), Double.valueOf(((UpcommingsBean) Upcomings.this.array_upcomings.get(position)).getStore_long()).doubleValue()), new LatLng(Double.valueOf(((UpcommingsBean) Upcomings.this.array_upcomings.get(position)).getShipping_lat()).doubleValue(), Double.valueOf(((UpcommingsBean) Upcomings.this.array_upcomings.get(position)).getShipping_long()).doubleValue()))))))).append(" miles").toString());
            } catch (Exception e2) {
                xxmilestext.setText("0.0 miles");
            }
            TextView destinationstextview = (TextView) convertView.findViewById(R.id.textview_drivers_destinations);
            destinationstextview.setTypeface(Typeface.createFromAsset(Upcomings.this.getActivity().getAssets(), "PROXIMANOVA-REGULAR.OTF"));
            ((TextView) convertView.findViewById(R.id.textview_drivers_contacts)).setText(((UpcommingsBean) Upcomings.this.array_upcomings.get(position)).getTelephone());
            ((TextView) convertView.findViewById(R.id.textview_drivers_customers)).setTypeface(Typeface.createFromAsset(Upcomings.this.getActivity().getAssets(), "PROXIMANOVA-REGULAR.OTF"));
            TextView completestextview = (TextView) convertView.findViewById(R.id.textview_drivers_completes);
            completestextview.setVisibility(View.GONE);
            completestextview.setTypeface(Typeface.createFromAsset(Upcomings.this.getActivity().getAssets(), "PROXIMANOVA-REGULAR.OTF"));
            TextView cusstextview = (TextView) convertView.findViewById(R.id.textview_drivers_customes);
            cusstextview.setText(((UpcommingsBean) Upcomings.this.array_upcomings.get(position)).getCustomer_name());
            cusstextview.setTypeface(Typeface.createFromAsset(Upcomings.this.getActivity().getAssets(), "PROXIMANOVA-SEMIBOLD.OTF"));
            storesTextview.setTypeface(Typeface.createFromAsset(Upcomings.this.getActivity().getAssets(), "PROXIMANOVA-SEMIBOLD.OTF"));
            TextView sourcestextview = (TextView) convertView.findViewById(R.id.textview_drivers_sources);
            sourcestextview.setTypeface(Typeface.createFromAsset(Upcomings.this.getActivity().getAssets(), "PROXIMANOVA-REGULAR.OTF"));
            reviewstextview.setTypeface(Typeface.createFromAsset(Upcomings.this.getActivity().getAssets(), "PROXIMANOVA-SEMIBOLD.OTF"));
            if (((UpcommingsBean) Upcomings.this.array_upcomings.get(position)).getIs_reviewed().equals("1")) {
                reviewtextviews.setText("Reviewed");
            } else {
                reviewtextviews.setText("Review Order");
            }
            storesTextview.setText(((UpcommingsBean) Upcomings.this.array_upcomings.get(position)).getStore_name());
            ticksimageview.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Upcomings.this.orderid = ((UpcommingsBean) Upcomings.this.array_upcomings.get(position)).getOrder_id();
                    if (Constants.isInternetOn(Upcomings.this.getActivity())) {
                        Upcomings.this.acceptespos = position;
                        Upcomings.this.Accepts();
                        return;
                    }
                    Constants.showMessage("No Internet Available", Upcomings.this.getActivity());
                }
            });
            reviewstextview.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((UpcommingsBean) Upcomings.this.array_upcomings.get(position)).getIs_reviewed().equals("0")) {
                        oi = Integer.parseInt(((UpcommingsBean) Upcomings.this.array_upcomings.get(position)).getOrder_id());
                        dialog = new Dialog(Upcomings.this.getActivity());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                        dialog.setCancelable(true);
                        dialog.setContentView(R.layout.listdialogs);
                        ListView listview = (ListView) Upcomings.this.dialog.findViewById(R.id.listview_dialog);
                        TextView textviewdone = (TextView) Upcomings.this.dialog.findViewById(R.id.textviewdone);
                        TextView textviewlater = (TextView) Upcomings.this.dialog.findViewById(R.id.textviewlater);
                        textviewdone.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ArrayList<products> cheched_product = new ArrayList<products>();
                                ArrayList<products> selected_product = new ArrayList<products>();
                                Cursor cursur_cp = Upcomings.this.myDbHelper.getAllProducts();
                                if (cursur_cp.moveToFirst()) {
                                    do {
                                        String data = cursur_cp.getString(cursur_cp.getColumnIndex("order_id"));
                                        orders = Integer.parseInt(data);
                                        cheched_product.add(new products(cursur_cp.getString(cursur_cp.getColumnIndex("product_id")), "", "", "", "", "", cursur_cp.getString(cursur_cp.getColumnIndex("order_id")), true));
                                    } while (cursur_cp.moveToNext());
                                }
                                cursur_cp.close();
                                for (int zz = 0; zz < cheched_product.size(); zz++) {
                                    if (oi == Integer.parseInt(((products) cheched_product.get(zz)).getorderi())) {
                                        String pid = ((products) cheched_product.get(zz)).getProduct_id();
                                        for (int i = 0; i < ((UpcommingsBean) array_upcomings.get(position)).getProducts().size(); i++) {
                                            if (((products) ((UpcommingsBean) array_upcomings.get(position)).getProducts().get(i)).getProduct_id().equals(pid)) {
                                                selected_product.add((products) ((UpcommingsBean) array_upcomings.get(position)).getProducts().get(i));
                                            }
                                        }
                                    }
                                }
                                Log.v("LogView", "Selected List Length   :" + selected_product.size());
                                confrimReviewOrder(position, oi, selected_product);
                            }
                        });
                        textviewlater.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        listview.setAdapter(new listAdapter(UpcommingsAdapters.this.scontext, ((UpcommingsBean) Upcomings.this.array_upcomings.get(position)).getProducts()));
                        dialog.show();
                        return;
                    }
                    oneButtonAlert("Sorry", "Reviewed order can not be edit", "Ok");
                }
            });
            crossimageview.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderid = ((UpcommingsBean) array_upcomings.get(position)).getOrder_id();
                    if (Constants.isInternetOn(getActivity())) {
                        UPCOMMINGSR();
                    } else {
                        Constants.errordialogs("Alert", "Please Connect to Internet", getActivity());
                    }
                    if (((UpcommingsBean) array_upcomings.get(position)).getOrder_id().equals(orderid)) {
                        array_upcomings.remove(position);
                    }
                }
            });
            datetextview.setText(simpleDateFormat.format(ds));
            orderstextview.setText("Order no. #" + Html.fromHtml("<font color='#0C080E'>" + ((UpcommingsBean) array_upcomings.get(position)).getOrder_id() + "</font>"), BufferType.SPANNABLE);
            sourcestextview.setText(((UpcommingsBean) array_upcomings.get(position)).getStore_address());
            destinationstextview.setText(new StringBuilder(String.valueOf(((UpcommingsBean) array_upcomings.get(position)).getShipping_address_1())).append("\n ").append(((UpcommingsBean) array_upcomings.get(position)).getShipping_city()).append(", ").append(((UpcommingsBean) array_upcomings.get(position)).getShipping_postcode()).toString());
            System.out.print("enwimage" + ((UpcommingsBean) array_upcomings.get(position)).getLogo_url());
            return convertView;
        }
    }

    private class listAdapter extends ArrayAdapter<products> {
        Cursor cursur_all = null;
        private LayoutInflater inflater;
        List<products> planets = new ArrayList<products>();

        public listAdapter(Context context, List<products> planetList) {
            super(context, R.layout.listsdialog, planetList);
            planets = planetList;
            inflater = LayoutInflater.from(context);
        }

        @NonNull
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            TextView storename;
            CheckBox checkBox;
            ImageView storeimage;
            TextView totals;
            products planet = (products) getItem(position);
            cursur_all = myDbHelper.getAllProducts();
            ArrayList<products> productss = new ArrayList<products>();
            if (cursur_all.moveToFirst()) {
                do {
                    String data = cursur_all.getString(cursur_all.getColumnIndex("order_id"));
                    orders = Integer.parseInt(data);
                    System.out.println("assssssssssssssssssssss" + cursur_all.getCount() + "            " + orders);
                    productss.add(new products(cursur_all.getString(cursur_all.getColumnIndex("product_id")), "", "", "", "", "", cursur_all.getString(cursur_all.getColumnIndex("order_id")), true));
                } while (cursur_all.moveToNext());
            }
            cursur_all.close();
            // convertView = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.listsdialog, null);
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
                            myDbHelper.addStoreUpcoming((UpcommingsBean) array_upcomings.get(positions), planet.getProduct_id(), oi);
                        } else if (oi == Integer.parseInt(planet.getorderi())) {
                            myDbHelper.deleteProductByid(planet.getProduct_id(), planet.getorderi());
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
                if (oi == Integer.parseInt(((products) productss.get(zz)).getorderi())) {
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
            Upcomings.this.imageLoader.displayImage(((products) planets.get(position)).getImage(), storeimage, selectsoptions);
            String price = String.format("%.2f", new Object[]{Double.valueOf(planet.getPrice())});
            Double quantity = Double.valueOf(planet.getQuantity());
            totals.setText(quantity + " X " + price + " = " + String.format("%.2f", new Object[]{Double.valueOf(planet.getTotal())}));
            return convertView;
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        pagenumber = 1;
        totalpage = 1;
        View rootview = inflater.inflate(R.layout.upcoming_listing, null);
        myDbHelper = new Databasehelper(getActivity());
        try {
            myDbHelper.createDatabase();
            myDbHelper.openDatabase();
            imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
            selectsoptions = new Builder().showImageOnLoading((int) R.drawable.default_store_icon).showImageForEmptyUri((int) R.drawable.default_store_icon).showImageOnFail((int) R.drawable.default_store_icon).cacheInMemory(true).displayer(new RoundedBitmapDisplayer(20, 10)).cacheOnDisk(true).considerExifParams(true).build();
            preferences = getActivity().getSharedPreferences(Constants.INSTAFRESH, 0);
            StrictMode.setThreadPolicy(new ThreadPolicy.Builder().permitAll().build());
            String logindatass = preferences.getString(Constants.LOGIN, null);
            try {
                Type testType = new TypeToken<JSONObject>() {
                }.getType();
                String logindatas = preferences.getString(Constants.LOGIN, "");
                if (logindatas != null && logindatas.length() > 0) {
                    JSONObject jSONObject = (JSONObject) gson.fromJson(logindatas, testType);
                }
            } catch (Exception e) {
            }
            RelativeLayout selectslayout = (RelativeLayout) rootview.findViewById(R.id.headers);
            this.noitemlay = (RelativeLayout) rootview.findViewById(R.id.noitemlay);
            RelativeLayout framelayouts = (RelativeLayout) rootview.findViewById(R.id.linearslayout);
            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                getActivity().getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            }
            LayoutParams p = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            p.addRule(RelativeLayout.BELOW, selectslayout.getId());
            framelayouts.setLayoutParams(p);
            Log.e("assssssss", "nnnnnnnn");
            homes = (ImageView) rootview.findViewById(R.id.homes);
            listview = (ListView) rootview.findViewById(R.id.listupcoming);
            progress_more = (RelativeLayout) rootview.findViewById(R.id.progressbas);
            ((ImageView) rootview.findViewById(R.id.editicon)).setVisibility(View.INVISIBLE);
            TextView textview = (TextView) rootview.findViewById(R.id.appname);
            textview.setText("Upcoming Delivery");
            array_upcomings = new ArrayList();
            upcommingsadapters = new UpcommingsAdapters(getActivity());
            textview.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "PROXIMANOVA-SEMIBOLD.OTF"));
            if (Constants.isInternetOn(getActivity())) {
                getUpcomming();
            } else {
                Constants.errordialogs("Alert", "Please Connect to Internet", getActivity());
            }
            listview.setAdapter(upcommingsadapters);
            initview();
            setlisteners();
            return rootview;
        } catch (IOException e2) {
            throw new Error("Unable to create database");
        }
    }

    void initview() {
        this.listview.setOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int count = listview.getCount();
                System.out.println("threshold" + count);
                if (scrollState == 0 && Upcomings.this.listview.getLastVisiblePosition() >= count - 1) {
                    System.out.println("on scrollll" + state);
                    if (!state) {
                        return;
                    }
                    if (Constants.isInternetOn(getActivity())) {
                        state = false;
                        loadmoreData();
                        return;
                    }
                    Constants.showMessage("No Internet", getActivity());
                }
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    void setlisteners() {
        homes.setOnClickListener(listeners);
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
        int kmInDec = Integer.valueOf(decimalFormat.format(km));
        Log.i("Radius Value", valueResult + "   KM  " + kmInDec + " Meter   " + Integer.valueOf(decimalFormat.format(valueResult % 1000.0d)));
        return ((double) 6371) * c;
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

    private void confrimReviewOrder(final int position, int order_id, ArrayList<products> selected_product) {
        if (Constants.isInternetOn(getActivity())) {
            final Dialog slid_dialog = new Dialog(getActivity());
            slid_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            slid_dialog.setCancelable(false);
            slid_dialog.setCanceledOnTouchOutside(false);
            slid_dialog.setContentView(R.layout.submit_review_dialog);
            slid_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            //slid_dialog.getWindow().getAttributes().windowAnimations = R.style.Animations.SmileWindow;
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
                    if (Constants.isInternetOn(Upcomings.this.getActivity())) {
                        if (arrayList.size() == 0) {
                            cancel = true;
                            array_upcomings.remove(position);
                            upcommingsadapters.notifyDataSetChanged();
                            if (array_upcomings.size() == 0) {
                                noitemlay.setVisibility(View.VISIBLE);
                            }
                        } else {
                            cancel = false;
                        }
                        reviewOrderRequest(i2, arrayList);
                        reviewd_position = position;
                        return;
                    }
                    Constants.errordialogs("Alert", "Please Connect to Internet", getActivity());
                }
            });
            btn_later.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    slid_dialog.dismiss();
                    dialog.dismiss();
                }
            });
            slid_dialog.show();
            return;
        }
        Constants.errordialogs("Alert", "Please Connect to Internet", getActivity());
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
       // new VolleyRequest(getActivity(), this, null).makeRequest(Request.Method.POST, URL.REVIEW_ORDER.getUrl(), null, null, "", "REVIEW_ORDER", true);
    }

    void getUpcomming() {
        this.driverid = this.preferences.getString(Constants.driverid, "");
        // String access_token = getActivity().getSharedPreferences(Constants.INSTAFRESH, 0).getString(Constants.ACCESS_TOKEN, "");
        if (this.pagenumber == 1) {
            //System.out.println("url  " + URL.GETUPCOMINGS.getUrl() + "?driver_id=" + this.preferences.getString(Constants.driverid, "") + "&access_token=" + access_token + "&time_zone=" + TimeZone.getDefault().getID() + "&page=" + this.pagenumber);
            new VolleyRequest(getActivity(), this, null).makeArrayRequest(Request.Method.GET, URL.GETUPCOMINGS.getUrl(), null, null, "", "DELIVERY", true);
            return;
        }
        new VolleyRequest(getActivity(), this, null).makeArrayRequest(Request.Method.GET, new StringBuilder(String.valueOf(URL.GETUPCOMINGS.getUrl())).append("&page=").append(this.pagenumber).toString(), null, null, "", "DELIVERY", false);
    }

    void Accepts() {
        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        Map<String, String> params = new HashMap<String, String>();
        params.put("status", "shipped");

        String url = String.format(URL.ACCEPTORDERS.getUrl(), this.orderid);
        CustomRequest objectRequest = new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                pDialog.hide();
                try {
                    Log.v("ACCEPTED", acceptespos + " " + response.toString());
                    if (((UpcommingsBean) array_upcomings.get(acceptespos)).getOrder_id().equals(orderid)) {
                        Log.v("Before ACCEPTED", array_upcomings.toString());
                        array_upcomings.remove(acceptespos);
                    }
                    Log.v("After ACCEPTED", array_upcomings.toString());
                    upcommingsadapters.notifyDataSetChanged();
                    if (array_upcomings.size() == 0) {
                        noitemlay.setVisibility(View.VISIBLE);
                        return;
                    }
                    return;
                } catch (Exception e3) {
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

    void UPCOMMINGSR() {
        JSONObject js = new JSONObject();
        String access_token = this.preferences.getString(Constants.ACCESS_TOKEN, "");
        this.driverid = this.preferences.getString(Constants.driverid, "");
        try {
            js.put("order_id", this.orderid);
            js.put(Constants.driverid, this.driverid);
            js.put("access_token", access_token);
        } catch (Exception e) {
        }
        new VolleyRequest(getActivity(), this, null).makeArrayRequest(Request.Method.GET, URL.UPCOMMINGSR.getUrl(), null, null, "", "REJECTS", true);
    }

    public void onTaskComplete(String tag, JSONArray response) {
        int z;
        JSONObject obj = new JSONObject();
        if (tag.equals("DELIVERY")) {
            try {
                obj.put("result", response);
                if (response.length() > 0) {
                    JSONArray items = obj.getJSONArray("result");
                    if (items.length() == 0) {
                        this.progress_more.setVisibility(View.GONE);
                        if (this.array_upcomings.size() == 0) {
                            this.noitemlay.setVisibility(View.VISIBLE);
                            return;
                        }
                        return;
                    }
                    if (!this.loadmore) {
                        this.loadmore = true;
                        // this.totalpage = Integer.parseInt(metas.getString("pageCount"));
                        this.totalpage += 1;
                    }
                    this.state = true;
                    this.noitemlay.setVisibility(View.GONE);
                    for (z = 0; z < items.length(); z++) {
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
                        String str = order_id;

                        JSONArray metas = os.getJSONArray("meta_data");
                        for (int m = 0; m < metas.length(); m++) {
                            JSONObject meta = metas.getJSONObject(m);
                            if (meta.getString("key").equals("_deliveryman_id") && meta.getString("value").equals(this.driverid)) {
                                this.array_upcomings.add(new UpcommingsBean(str, customer_name, store_address, store_lat, store_long, logo_url, total, store_name, status, telephone, shipping_firstname, shipping_lastname, shipping_address_1, shipping_address_2, shipping_city, shipping_postcode, assign_count, product_count, created_at, shipping_lat, shipping_long, "0", arrayproducts));
                            }
                        }
                    }
                    if (array_upcomings.size() == 0) {
                        this.noitemlay.setVisibility(View.VISIBLE);
                        return;
                    }
                    this.upcommingsadapters.notifyDataSetChanged();
                    this.progress_more.setVisibility(View.GONE);
                    return;
                }
                if (this.array_upcomings.size() == 0) {
                    this.noitemlay.setVisibility(View.VISIBLE);
                }
                if (obj.getJSONArray("errors").getJSONObject(0).getString("message").equals("Invalid Access Token.")) {
                    Constants.dialogs("Alert", "Invalid Access Token.", null, getActivity(), "");
                } else {
                    Constants.errordialogs("Alert", obj.getJSONArray("errors").getJSONObject(0).getString("message"), getActivity());
                }
                this.progress_more.setVisibility(View.GONE);
                return;
            } catch (Exception e) {
                if (this.array_upcomings.size() == 0) {
                    this.noitemlay.setVisibility(View.VISIBLE);
                }
                this.progress_more.setVisibility(View.GONE);
                return;
            }
        }
        if (tag.equals("REVIEW_ORDER")) {
            try {
                this.dialog.dismiss();
                obj.put("result", response);
                if (!obj.getString("success").equals("true")) {
                    if (obj.getJSONArray("errors").getJSONObject(0).getString("message").equals("Invalid Access Token.")) {
                        Constants.dialogs("Alert", "Invalid Access Token.", null, getActivity(), "");
                        return;
                    }
                    Constants.errordialogs("Alert", obj.getJSONArray("errors").getJSONObject(0).getString("message"), getActivity());
                    return;
                } else if (!this.cancel) {
                    for (z = 0; z < this.array_upcomings.size(); z++) {
                        if (z == this.reviewd_position) {
                            if (this.oi != Integer.parseInt(((UpcommingsBean) this.array_upcomings.get(this.reviewd_position)).getOrder_id())) {
                                ((UpcommingsBean) this.array_upcomings.get(z)).setIs_reviewed(((UpcommingsBean) this.array_upcomings.get(this.reviewd_position)).getIs_reviewed());
                            } else if (((UpcommingsBean) this.array_upcomings.get(z)).getIs_reviewed().equals("0")) {
                                boolean zz2 = this.myDbHelper.ComStore("1", this.oi);
                                ((UpcommingsBean) this.array_upcomings.get(z)).setIs_reviewed("1");
                                System.out.println("CompleteComplete");
                            } else {
                                ((UpcommingsBean) this.array_upcomings.get(z)).setIs_reviewed(((UpcommingsBean) this.array_upcomings.get(this.reviewd_position)).getIs_reviewed());
                            }
                        }
                        this.upcommingsadapters.notifyDataSetChanged();
                    }
                    return;
                } else {
                    return;
                }
            } catch (Exception e2) {
                return;
            }
        }
    }

    private void loadmoreData() {
        int listposition = this.array_upcomings.size();
        this.pagenumber++;
        if (this.pagenumber > this.totalpage) {
            Constants.showMessage("no more data", getActivity());
            return;
        }
        this.progress_more.setVisibility(View.VISIBLE);
        try {
            if (Constants.isInternetOn(getActivity())) {
                getUpcomming();
            } else {
                Constants.errordialogs("Alert", "Please Connect to Internet", getActivity());
            }
        } catch (Exception e) {
        }
    }
}
