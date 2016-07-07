package com.example.coupondunia.goodbox;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.coupondunia.goodbox.database.DatabaseHelper;
import com.example.coupondunia.goodbox.database.SMSOfflineDatabase;

import org.json.JSONObject;

import java.util.ArrayList;

public class ConnectivityReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (isOnline(context)) {
            getDataFromDatabase();
            //TODO to upload data from database when there is no internet connection and we save the sms to database
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in air plan mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }

    public void getDataFromDatabase() {
        try {
            SQLiteDatabase db = DatabaseHelper.getInstance().getDatabseManipulater();
            Cursor cursor = db.rawQuery("SELECT * FROM " + SMSOfflineDatabase.SMSTable.TABLE_NAME + " LIMIT 20", null);
            if (cursor != null && cursor.getCount() > 0) {

                ArrayList<JSONObject> list = new ArrayList<>();
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                    Log.d("draft_object", cursor.getString(cursor.getColumnIndexOrThrow(SMSOfflineDatabase.SMSTable.SMS_DATA)));
                    JSONObject object = new JSONObject(cursor.getString(cursor.getColumnIndexOrThrow(SMSOfflineDatabase.SMSTable.SMS_DATA)));
                    list.add(object);
                }
                cursor.close();
            }
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
