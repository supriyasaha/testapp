package com.example.coupondunia.goodbox.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.coupondunia.goodbox.GoodBoxApplication;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE = "sms.db";
    public static final int DATABASE_VERSION = 2;
    public static final int BILL_STATUS_DRAFT = 0;
    public static final int BILL_STATUS_UPLOADED = 1;


    private static final String SMS_OFFLINE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + SMSOfflineDatabase.SMSTable.TABLE_NAME + " ("
                    + SMSOfflineDatabase.SMSTable.ENTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + SMSOfflineDatabase.SMSTable.SMS_DATA + " TEXT"
                    + ")";


    private static DatabaseHelper mDatabaseHelper = new DatabaseHelper(GoodBoxApplication.context);
    private SQLiteDatabase mDatabaseManipulator;


    public DatabaseHelper(Context context) {
        super(context, DATABASE, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance() {
        return mDatabaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SMS_OFFLINE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public synchronized SQLiteDatabase getDatabseManipulater() {
        if (mDatabaseManipulator == null) {
            mDatabaseManipulator = getWritableDatabase();
        }
        return mDatabaseManipulator;
    }
}
