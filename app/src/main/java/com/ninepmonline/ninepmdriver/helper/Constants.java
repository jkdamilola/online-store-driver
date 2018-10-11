package com.ninepmonline.ninepmdriver.helper;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.ninepmonline.ninepmdriver.BaseScreen;
import com.ninepmonline.ninepmdriver.R;
import java.io.ByteArrayOutputStream;
import java.util.regex.Pattern;

import static android.widget.ListPopupWindow.MATCH_PARENT;
import static android.widget.ListPopupWindow.WRAP_CONTENT;

public class Constants {
    public static final String ACCESS_TOKEN = "access__token";
    public static final String CARDDETAIL = "card_detail";
    public static final String INSTAFRESH = "instafreshDrivers";
    public static final String LOGIN = "login_data";
    public static final String LOGIN_TYPE = "type";
    public static String LONGITUDE = "0.0";
    public static final String POSTAL_CODE = "postal_code";
    public static final String SENDER_ID = "264580327288";
    public static final String SIGNUP = "signup";
    public static final String STORE_ID = "store_id";
    public static final String STORE_NAME = "store_name";
    public static final String USER_ID = "user_id";
    public static final String USER_TYPE = "user_type";
    private static Dialog alert1 = null;
    public static String[] dlATITUDE = null;
    public static final String driverid = "driver_id";
    public static final String driverids = "driver_ids";
    public static String lATITUDE = "0.0";
    private static SharedPreferences preferences;
    public static String select;
    public static String[] slATITUDE;
    public static boolean web_service_status = false;
    public static String CONSUMER_KEY = "ck_edd81aad0672e5e31b865b278584a72c5423873f";
    public static String CONSUMER_SECRET = "cs_4b7f602efa1b31279737c7f9860eae0adfedf9fb";
    String[] longitudes;

    class AnonymousClass1 implements OnClickListener {
        private final /* synthetic */ Dialog val$alert;

        AnonymousClass1(Dialog dialog) {
            this.val$alert = dialog;
        }

        public void onClick(View v) {
            this.val$alert.dismiss();
        }
    }

    class AnonymousClass2 implements OnClickListener {
        private final /* synthetic */ Activity val$activity;

        AnonymousClass2(Activity activity) {
            this.val$activity = activity;
        }

        public void onClick(View v) {
            Constants.alert1.dismiss();
            Constants.preferences = this.val$activity.getSharedPreferences(Constants.INSTAFRESH, 0);
            Constants.preferences.edit().putString(Constants.ACCESS_TOKEN, "").commit();
            Constants.preferences.edit().putString(Constants.LOGIN, "").commit();
            Intent intent = new Intent(this.val$activity, BaseScreen.class);
            intent.setFlags(872448000);
            this.val$activity.startActivity(intent);
        }
    }

    public static void showMessage(String message, Context context) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.getView().setBackgroundResource(R.drawable.custom_toast);
        toast.show();
    }

    public static boolean validateEmail(String email) {
        return Pattern.compile("[A-Z0-9._%+-]+@[A-Z0-9.-]+.[A-Z]{2,4}", Pattern.CASE_INSENSITIVE).matcher(email).matches();
    }

    public static Bitmap getBlurredBitmap(Bitmap original, int radius) {
        if (radius < 1) {
            return null;
        }
        int width = original.getWidth();
        int height = original.getHeight();
        int wm = width - 1;
        int hm = height - 1;
        int wh = width * height;
        int div = (radius + radius) + 1;
        int[] r = new int[wh];
        int[] g = new int[wh];
        int[] b = new int[wh];
        int[] vmin = new int[Math.max(width, height)];
        int[] vmax = new int[Math.max(width, height)];
        int[] dv = new int[(div * 256)];
        for (int i = 0; i < div * 256; i++) {
            dv[i] = i / div;
        }
        int[] blurredBitmap = new int[wh];
        original.getPixels(blurredBitmap, 0, width, 0, 0, width, height);
        int yw = 0;
        int yi = 0;
        for (int y = 0; y < height; y++) {
            int rsum = 0;
            int gsum = 0;
            int bsum = 0;
            for (int i = -radius; i <= radius; i++) {
                int p = blurredBitmap[Math.min(wm, Math.max(i, 0)) + yi];
                rsum += (16711680 & p) >> 16;
                gsum += (MotionEventCompat.ACTION_POINTER_INDEX_MASK & p) >> 8;
                bsum += p & 255;
            }
            for (int x = 0; x < width; x++) {
                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];
                if (y == 0) {
                    vmin[x] = Math.min((x + radius) + 1, wm);
                    vmax[x] = Math.max(x - radius, 0);
                }
                int p1 = blurredBitmap[vmin[x] + yw];
                int p2 = blurredBitmap[vmax[x] + yw];
                rsum += ((16711680 & p1) - (16711680 & p2)) >> 16;
                gsum += ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & p1) - (MotionEventCompat.ACTION_POINTER_INDEX_MASK & p2)) >> 8;
                bsum += (p1 & 255) - (p2 & 255);
                yi++;
            }
            yw += width;
        }
        for (int x = 0; x < width; x++) {
            int bsum = 0;
            int gsum = 0;
            int rsum = 0;
            int yp = (-radius) * width;
            for (int i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;
                rsum += r[yi];
                gsum += g[yi];
                bsum += b[yi];
                yp += width;
            }
            yi = x;
            for (int y = 0; y < height; y++) {
                blurredBitmap[yi] = ((ViewCompat.MEASURED_STATE_MASK | (dv[rsum] << 16)) | (dv[gsum] << 8)) | dv[bsum];
                if (x == 0) {
                    vmin[y] = Math.min((y + radius) + 1, hm) * width;
                    vmax[y] = Math.max(y - radius, 0) * width;
                }
                int p1 = x + vmin[y];
                int p2 = x + vmax[y];
                rsum += r[p1] - r[p2];
                gsum += g[p1] - g[p2];
                bsum += b[p1] - b[p2];
                yi += width;
            }
        }
        return Bitmap.createBitmap(blurredBitmap, width, height, Config.RGB_565);
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int round) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        float roundPx = (float) round;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.GREEN);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static boolean isInternetOn(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo state : info) {
                    if (state.getState() == State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void errordialogs(String title, String message, final Activity activity) {
        final Dialog alert = new Dialog(activity);
        alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alert.setCancelable(false);
        alert.setCanceledOnTouchOutside(false);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alert.setContentView(R.layout.error_popup);
        TextView tv_error_header = (TextView) alert.findViewById(R.id.tv_error_header);
        TextView tv_error_message = (TextView) alert.findViewById(R.id.tv_error_message);
        Button button_ok = (Button) alert.findViewById(R.id.button_ok);
        tv_error_header.setText(title);
        tv_error_message.setText(message);
        button_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        tv_error_header.setTypeface(Typeface.createFromAsset(activity.getAssets(), "PROXIMANOVA-SEMIBOLD.OTF"));
        tv_error_message.setTypeface(Typeface.createFromAsset(activity.getAssets(), "PROXIMANOVA-REGULAR.OTF"));
        button_ok.setTypeface(Typeface.createFromAsset(activity.getAssets(), "PROXIMANOVA-REGULAR.OTF"));
        alert.show();
    }

    public static Dialog dialogs(String title, String message, Fragment activity1, final Activity activity, String yes) {
        alert1 = new Dialog(activity);
        alert1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alert1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFe")));
        alert1.setContentView(R.layout.invalidtoken);
        alert1.getWindow().setLayout(MATCH_PARENT, WRAP_CONTENT);
        TextView token = (TextView) alert1.findViewById(R.id.textview_token);
        Button login_btn = (Button) alert1.findViewById(R.id.buttonlogin);
        TextView login = (TextView) alert1.findViewById(R.id.textview_login);
        login_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.alert1.dismiss();
                Constants.preferences = activity.getSharedPreferences(Constants.INSTAFRESH, 0);
                Constants.preferences.edit().putString(Constants.ACCESS_TOKEN, "").commit();
                Constants.preferences.edit().putString(Constants.LOGIN, "").commit();
                Intent intent = new Intent(activity, BaseScreen.class);
                intent.setFlags(872448000);
                activity.startActivity(intent);
            }
        });
        token.setTypeface(Typeface.createFromAsset(activity.getAssets(), "PROXIMANOVA-REGULAR.OTF"));
        login_btn.setTypeface(Typeface.createFromAsset(activity.getAssets(), "PROXIMANOVA-REGULAR.OTF"));
        login.setTypeface(Typeface.createFromAsset(activity.getAssets(), "PROXIMANOVA-REGULAR.OTF"));
        alert1.setCancelable(false);
        alert1.setCanceledOnTouchOutside(false);
        alert1.show();
        return alert1;
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(CompressFormat.JPEG, 100, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), 0);
        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }
}
