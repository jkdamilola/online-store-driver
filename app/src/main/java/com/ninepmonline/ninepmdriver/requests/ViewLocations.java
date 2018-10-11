package com.ninepmonline.ninepmdriver.requests;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.internal.view.SupportMenu;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ninepmonline.ninepmdriver.R;
import com.ninepmonline.ninepmdriver.helper.Constants;
import com.ninepmonline.ninepmdriver.helper.DirectionsJSONParser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

public class ViewLocations extends FragmentActivity implements OnMapReadyCallback {
    ArrayList<String> daddress;
    ArrayList<String> dlatitude;
    private GoogleMap googleMap;
    ArrayList<String> logourl;
    private Polyline pickup_polyline;
    private SharedPreferences preferences;
    ArrayList<String> saddress;
    ArrayList<String> slatitude;
    ArrayList<String> storename;
    ArrayList<String> username;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap = googleMap;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {
        private DownloadTask() {
        }

        protected String doInBackground(String... url) {
            String data = "";
            try {
                data = ViewLocations.this.downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(new String[]{result});
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        private ParserTask() {
        }

        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            List<List<HashMap<String, String>>> routes = null;
            try {
                routes = new DirectionsJSONParser().parse(new JSONObject(jsonData[0]));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            PolylineOptions lineOptions;
            int i = 0;
            PolylineOptions lineOptions2 = null;
            ArrayList<LatLng> points = null;
            while (i < result.size()) {
                try {
                    ArrayList<LatLng> points2 = new ArrayList();
                    try {
                        lineOptions = new PolylineOptions();
                        try {
                            List<HashMap<String, String>> path = (List) result.get(i);
                            for (int j = 0; j < path.size(); j++) {
                                HashMap<String, String> point = (HashMap) path.get(j);
                                double lat = Double.parseDouble((String) point.get("lat"));
                                double lng = Double.parseDouble((String) point.get("lng"));
                                Log.v("LogView", "loop lat : " + lat);
                                Log.v("LogView", "loop lng : " + lng);
                                points2.add(new LatLng(lat, lng));
                            }
                            lineOptions.addAll(points2);
                            lineOptions.width(5.0f);
                            lineOptions.color(SupportMenu.CATEGORY_MASK);
                            i++;
                            lineOptions2 = lineOptions;
                            points = points2;
                        } catch (Exception e) {
                            return;
                        }
                    } catch (Exception e2) {
                        lineOptions = lineOptions2;
                        return;
                    }
                } catch (Exception e3) {
                    lineOptions = lineOptions2;
                    ArrayList<LatLng> points2 = points;
                    return;
                }
            }
            ViewLocations.this.pickup_polyline = ViewLocations.this.googleMap.addPolyline(lineOptions2);
            lineOptions = lineOptions2;
        }
    }

    public class MarkerInfoWindowAdapter implements InfoWindowAdapter {
        private ImageLoader imageLoader = ImageLoader.getInstance();
        ArrayList<String> offer = new ArrayList();
        private DisplayImageOptions options;
        private DisplayImageOptions options2;

        public MarkerInfoWindowAdapter(int u) {
            this.imageLoader.init(ImageLoaderConfiguration.createDefault(ViewLocations.this));
            this.options = new Builder().showImageOnLoading((int) R.drawable.default_store_icon).showImageForEmptyUri((int) R.drawable.default_store_icon).showImageOnFail((int) R.drawable.default_store_icon).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
            this.options2 = new Builder().showImageOnLoading((int) R.drawable.default_profile).showImageForEmptyUri((int) R.drawable.default_profile).showImageOnFail((int) R.drawable.default_profile).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
        }

        public View getInfoWindow(Marker marker) {
            if (marker != null && marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
                marker.showInfoWindow();
            }
            return null;
        }

        public View getInfoContents(Marker marker) {
            View v = ViewLocations.this.getLayoutInflater().inflate(R.layout.markerwindows, null);
            TextView textview_mappricetext = (TextView) v.findViewById(R.id.textview_mappriceoffer);
            ImageView imageview_profile = (ImageView) v.findViewById(R.id.imageview_offers);
            ((TextView) v.findViewById(R.id.textview_offer_map_list_headings)).setText(marker.getTitle());
            textview_mappricetext.setText(marker.getSnippet());
            textview_mappricetext.setVisibility(View.GONE);
            if (marker.getSnippet().length() == 0) {
                this.imageLoader.displayImage(marker.getSnippet(), imageview_profile, this.options2);
            } else {
                this.imageLoader.displayImage(marker.getSnippet(), imageview_profile, this.options);
            }
            return v;
        }
    }

    public /* bridge */ /* synthetic */ View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(view, str, context, attributeSet);
    }

    public /* bridge */ /* synthetic */ View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(str, context, attributeSet);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewlocations);
        this.slatitude = getIntent().getStringArrayListExtra("slist");
        this.dlatitude = getIntent().getStringArrayListExtra("dlist");
        this.saddress = getIntent().getStringArrayListExtra("saddress");
        this.daddress = getIntent().getStringArrayListExtra("daddress");
        this.logourl = getIntent().getStringArrayListExtra("logourl");
        ImageView bak = (ImageView) findViewById(R.id.homes);
        this.storename = getIntent().getStringArrayListExtra("storename");
        this.username = getIntent().getStringArrayListExtra("username");
        bak.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ViewLocations.this.finish();
            }
        });
        try {
            initilizeMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initilizeMap() {
        try {
            if (this.googleMap == null) {
                int z;
                double latitude;
                double longitude;
                ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
                this.googleMap.getUiSettings().setAllGesturesEnabled(true);
                this.googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                this.preferences = getSharedPreferences(Constants.INSTAFRESH, 0);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                this.googleMap.setMyLocationEnabled(true);
                System.out.println("assssssssssssssssssssssssssss" + this.preferences.getString("lat", "0.0") + this.preferences.getString("lng", "0.0"));
                this.googleMap.addMarker(new MarkerOptions().title("driver").snippet(" ").position(new LatLng(Double.valueOf(this.preferences.getString("lat", "0.0")).doubleValue(), Double.valueOf(this.preferences.getString("lng", "0.0")).doubleValue())).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                for (z = 0; z < this.slatitude.size(); z++) {
                    System.out.println("assssssssssssssssssssssssssss" + ((String) this.slatitude.get(z)) + this.slatitude.size());
                    if (!((String) this.slatitude.get(z)).split(" ,")[0].equals("")) {
                        latitude = Double.valueOf(((String) this.slatitude.get(z)).split(" ,")[0]).doubleValue();
                        longitude = Double.valueOf(((String) this.slatitude.get(z)).split(" ,")[1]).doubleValue();
                        LatLng latLng = new LatLng(latitude, longitude);
                        this.googleMap.addMarker(new MarkerOptions().title((String) this.storename.get(z)).snippet("").position(new LatLng(latitude, longitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.storepin)));
                    }
                }
                z = 0;
                while (z < this.dlatitude.size()) {
                    System.out.println("assssssssssssssssssssssssssss" + ((String) this.dlatitude.get(z)) + this.dlatitude.size());
                    if (!(((String) this.dlatitude.get(z)).split(" ,")[0].equals("") || ((String) this.dlatitude.get(z)).split(" ,")[0].equals("null"))) {
                        latitude = Double.valueOf(((String) this.dlatitude.get(z)).split(" ,")[0]).doubleValue();
                        longitude = Double.valueOf(((String) this.dlatitude.get(z)).split(" ,")[1]).doubleValue();
                        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
                        this.googleMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f), 2000, null);
                        this.googleMap.addMarker(new MarkerOptions().title((String) this.daddress.get(z)).snippet("  ").position(new LatLng(latitude, longitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.shippingpin)));
                    }
                    z++;
                }
            }
            if (this.googleMap == null) {
                Toast.makeText(getApplicationContext(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
            }
            this.googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {
                public boolean onMarkerClick(Marker marker) {
                    if (!marker.getSnippet().equals(" ") && marker.getSnippet().equals("  ")) {
                        ViewLocations.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://maps.google.com/maps?saddr=" + ViewLocations.this.preferences.getString("lat", "0.0") + "," + ViewLocations.this.preferences.getString("lng", "0.0") + "&daddr=" + marker.getPosition().latitude + "," + marker.getPosition().longitude)));
                    }
                    return false;
                }
            });
        } catch (Exception e) {
        }
    }

    protected void onResume() {
        super.onResume();
        try {
            initilizeMap();
        } catch (Exception e) {
        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) new URL(strUrl).openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String str = "";
            while (true) {
                str = br.readLine();
                if (str == null) {
                    break;
                }
                sb.append(str);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            // Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    public void drawpath(LatLng origin, LatLng dest) {
        this.googleMap.clear();
        this.googleMap = null;
        initilizeMap();
        String url = getDirectionsUrl(origin, dest);
        new DownloadTask().execute(new String[]{url});
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String url = "https://maps.googleapis.com/maps/api/directions/" + "json" + "?" + new StringBuilder(String.valueOf(str_origin)).append("&").append("destination=" + dest.latitude + "," + dest.longitude).append("&").append("sensor=false").toString();
        Log.v("LogView", " getDirectionsUrl   : " + url);
        return url;
    }
}
