package com.example.android.justjava;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by vises_000 on 11/16/2015.
 */
public class OrderHelper extends SQLiteOpenHelper {

    // Database Related Constants
    private static final String DATABASE_NAME = "orderhistory";
    public static final String TABLE_NAME = "order";
    private static final int DATABASE_VERSION = 2;

    // Database Columns
    public static final String ID = "_id";
    public static final String ORDER_DATE = "order_date";
    public static final String CUST_NAME = "cust_name";
    public static final String ORDER_ID = "order_id";
    public static final String QUANTITY = "quantity";
    public static final String TOPPINGS = "toppings";
    public static final String TOTAL = "total";

    private static final String CREATE_TABLE = "create table " + TABLE_NAME + " (" + ID
            + " integer primary key autoincrement, " + ORDER_DATE + " text not null, "
            + CUST_NAME + " text not null, " + ORDER_ID + " integer not null, "
            + QUANTITY + " integer not null, " + TOPPINGS + " text not null, "
            + TOTAL + " integer not null);";
    private static final String DROP_TABLE = "drop table if exists " + TABLE_NAME;

    public OrderHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.v("MainActivity", "CONSTRUCTOR CALLED");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE);
            Log.v("MainActivity", "TABLE CREATED");
        } catch (Exception e) {
            Log.v("MainActivity", "" + e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DROP_TABLE);
            onCreate(db);
            Log.v("MainActivity", "TABLE UPGRADED");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
