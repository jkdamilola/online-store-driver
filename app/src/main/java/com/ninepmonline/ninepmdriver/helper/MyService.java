package com.ninepmonline.ninepmdriver.helper;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

public class MyService extends Service implements LocationListener {
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 5;
    private static final long MIN_TIME_BW_UPDATES = 10000;
    private String access_token = "";
    boolean isNetworkEnabled = false;
    public double latitude = 0.0d;
    Location location;
    protected LocationManager locationManager;
    private double longitude = 0.0d;
    private SharedPreferences preferences;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("ShowView", "Main Service onStartCommand");
        return Service.START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.v("ShowView", "Main Service on create");
        super.onCreate();
        updateLocation();
    }

    public void onStart(Intent intent, int startId) {
    }

    public IBinder onUnBind(Intent arg0) {
        return null;
    }

    public void onStop() {
    }

    public void onPause() {
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.locationManager != null) {
            this.locationManager.removeUpdates(this);
        }
    }

    private Location updateLocation() {
        try {
            this.locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            this.isNetworkEnabled = this.locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (this.isNetworkEnabled) {
                this.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0.0f, this);
                Log.v("ShowView", "Network");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.location;
    }

    public void onLocationChanged(Location location) {
        if (location != null) {
            this.latitude = location.getLatitude();
            this.longitude = location.getLongitude();
            this.preferences = getSharedPreferences(Constants.INSTAFRESH, 0);
            Editor editor = this.preferences.edit();
            editor.putString("lat", new StringBuilder(String.valueOf(location.getLatitude())).toString());
            editor.putString("lng", new StringBuilder(String.valueOf(location.getLongitude())).toString());
            editor.commit();
            Constants.lATITUDE = new StringBuilder(String.valueOf(this.latitude)).toString();
            Constants.LONGITUDE = new StringBuilder(String.valueOf(this.longitude)).toString();
            Log.v("ShowView", this.preferences.getString("lat", "0.0"));
            Log.v("ShowView", this.preferences.getString("lng", "0.0"));
            this.access_token = this.preferences.getString(Constants.ACCESS_TOKEN, "");
            if (!Constants.isInternetOn(this) || this.access_token.equals("")) {
                Log.v("LogView", "Net OFF");
                return;
            } else {
                Log.v("LogView", "Net On");
                return;
            }
        }
        Log.v("LogView", "In onLocationChange Location is null");
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onProviderDisabled(String provider) {
    }
}
