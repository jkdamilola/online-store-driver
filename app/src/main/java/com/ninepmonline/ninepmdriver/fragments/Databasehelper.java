package com.ninepmonline.ninepmdriver.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.ninepmonline.ninepmdriver.helper.AcceptesBeans;
import com.ninepmonline.ninepmdriver.helper.UpcommingsBean;
import com.ninepmonline.ninepmdriver.requests.products;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class Databasehelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "InstafreshDrivers.sqlite";
    public static final String DATABASE_PATH = "/data/data/com.ninepmonline.ninepmdriver/databases/";
    public static final int DATABASE_VERSION = 1;
    private static final String TABLE_Complete = "Complete";
    private static final String TABLE_STORE = "AcceptedOrders";
    private final Context myContext;
    private SQLiteDatabase myDataBase;

    public Databasehelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = context;
    }

    public void createDatabase() throws IOException {
        boolean dbExist = checkDataBase();
        System.out.println("DB Exist: " + dbExist);
        if(dbExist){
            //do nothing - database already exist
        } else {
            getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DATABASE_PATH + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            System.out.println("delete database file.");
        }
        if(checkDB != null){
            checkDB.close();
        }

        return checkDB != null;
    }

    private void copyDataBase() throws IOException {
        // Path to the just created empty db
        String outFileName = DATABASE_PATH + DATABASE_NAME;
        System.out.println("Output File: " + outFileName);
        OutputStream myOutput = new FileOutputStream(outFileName);
        InputStream myInput = this.myContext.getAssets().open(DATABASE_NAME);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        System.out.println("DB Exist: " + length);

        myInput.close();
        myOutput.flush();
        myOutput.close();
    }

    public void db_delete() {
        String myPath = DATABASE_PATH + DATABASE_NAME;
        File file = new File(myPath);
        if (file.exists()) {
            file.delete();
            System.out.println("delete database file.");
        }
    }

    public void openDatabase() throws SQLException {
        //Open the database
        String myPath = DATABASE_PATH + DATABASE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    public synchronized void closeDataBase() throws SQLException {
        if (this.myDataBase != null) {
            this.myDataBase.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            Log.v("Database Upgrade", "Database version higher than old.");
            db_delete();
        }
    }

    public boolean addStore(AcceptesBeans s, String productid, int order) {
        SQLiteDatabase q = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("order_id", Integer.valueOf(order));
        cv.put("product_id", productid);
        q.insertWithOnConflict(TABLE_STORE, null, cv, 5);
        Log.i("store ", productid);
        return true;
    }

    public boolean addStoreUpcoming(UpcommingsBean s, String productid, int order) {
        SQLiteDatabase q = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("order_id", Integer.valueOf(order));
        cv.put("product_id", productid);
        q.insertWithOnConflict(TABLE_STORE, null, cv, 5);
        Log.i("store ", productid);
        return true;
    }

    public boolean ComStore(String completes, int order) {
        SQLiteDatabase q = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("orderid", Integer.valueOf(order));
        cv.put("completes", completes);
        q.insertWithOnConflict(TABLE_Complete, null, cv, 5);
        return true;
    }

    public Cursor getAllProducts() {
        ArrayList<products> array_list = new ArrayList();
        Cursor res = getReadableDatabase().rawQuery("select * from AcceptedOrders", null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            products s = new products(res.getString(res.getColumnIndex("product_id")), "", "", "", "", "", "", true);
            array_list.add(s);
            System.out.println("store         " + s.getIsselect() + s.getProduct_id());
            res.moveToNext();
        }
        return res;
    }

    public Cursor getViewed(int order) {
        return getReadableDatabase().rawQuery("select * from Complete where orderid='" + order + "'", null);
    }

    public boolean updateproduct(AcceptesBeans s, String productid) {
        SQLiteDatabase q = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("order_id", s.getOrder_id());
        cv.put("productid", productid);
        q.update(TABLE_STORE, cv, "productid = ?", new String[]{productid});
        return true;
    }

    public int deleteProductByid(String id, String orderi) {
        SQLiteDatabase db = getReadableDatabase();
        Log.i("assssssssssssssssssss", id);
        return db.delete(TABLE_STORE, "product_id = " + id + " and order_id" + " = " + orderi, null);
    }

    public int deleteAccepted() {
        return getWritableDatabase().delete(TABLE_STORE, null, null);
    }

    public int deleteCompelet() {
        return getWritableDatabase().delete(TABLE_Complete, null, null);
    }
}
