package com.ninepmonline.ninepmdriver.requests;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ninepmonline.ninepmdriver.WebServices.AppController;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class VolleyRequest {
    private RequestCompleteListener<JSONObject> callback;
    private RequestCompleteListener<JSONArray> callbackArr;
    private ProgressDialog pDialog;
    boolean progress = true;
    HashMap<String, String> requestparams = null;

    public VolleyRequest(Context context, RequestCompleteListener<JSONObject> callback) {
        this.callback = callback;
        this.pDialog = new ProgressDialog(context);
        this.pDialog.setMessage("Loading...");
        this.pDialog.setCancelable(false);
    }

    public VolleyRequest(Context context, RequestCompleteListener<JSONArray> callback, String str) {
        this.callbackArr = callback;
        this.pDialog = new ProgressDialog(context);
        this.pDialog.setMessage("Loading...");
        this.pDialog.setCancelable(false);
    }

    private void showProgressDialog() {
        if (!this.pDialog.isShowing()) {
            this.pDialog.show();
        }
    }

    private void hideProgressDialog() {
        if (this.pDialog.isShowing()) {
            this.pDialog.hide();
        }
    }

    public void makeRequest(int method, String URL, JSONObject json, HashMap<String, String> params, String access_token, final String tag, final boolean progress) {
        this.progress = progress;
        this.requestparams = params;
        if (progress) {
            showProgressDialog();
        }
        Log.d("Response ", method + " - " + URL);
        final String str = access_token;
        JsonObjectRequest objectRequest = new JsonObjectRequest(method, URL, json, new Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("Response ", response.toString());
                if (progress) {
                    hideProgressDialog();
                }
               callback.onTaskComplete(tag, response);
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Errorrrrrr ", error);
                if (progress) {
                    hideProgressDialog();
                }
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                return requestparams;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap();
                headers.put("Content-Type", "application/json");
                if (!str.equals("")) {
                    headers.put("Access-Token", str);
                }
                return headers;
            }
        };
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(60000, 1, 1.0f));
        AppController.getInstance().addToRequestQueue(objectRequest, "REQUEST");
    }

    public void makeArrayRequest(int method, String URL, JSONArray json, HashMap<String, String> params, String access_token, final String tag, final boolean progress) {
        this.progress = progress;
        this.requestparams = params;
        if (progress) {
            showProgressDialog();
        }
        Log.d("Response ", method + " - " + URL);
        final String str = access_token;
        JsonArrayRequest objectRequest = new JsonArrayRequest(method, URL, json, new Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.v("Response ", response.toString());
                if (progress) {
                    hideProgressDialog();
                }
                callbackArr.onTaskComplete(tag, response);
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Errorrrrrr ", error);
                if (progress) {
                    hideProgressDialog();
                }
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                return requestparams;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap();
                headers.put("Content-Type", "application/json");
                if (!str.equals("")) {
                    headers.put("Access-Token", str);
                }
                return headers;
            }
        };
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(60000, 1, 1.0f));
        AppController.getInstance().addToRequestQueue(objectRequest, "REQUEST");
    }
}
