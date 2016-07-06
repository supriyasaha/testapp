package com.example.coupondunia.goodbox;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.coupondunia.goodbox.database.DatabaseHelper;
import com.example.coupondunia.goodbox.database.SMSOfflineDatabase;
import com.example.coupondunia.goodbox.retrofitservice.RestCallBack;
import com.example.coupondunia.goodbox.retrofitservice.RestClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Response;


public class TriggerActionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int id = 0;
        MessageModel messageModel = null;
        Bundle args = intent.getExtras();
        if (args != null) {
            id = args.getInt("Notification_id");
            messageModel = args.getParcelable("messageObject");
        }
        if (intent.getAction().equalsIgnoreCase(SmsReciever.YES_ACTION)) {
            Toast.makeText(context, "yes received", Toast.LENGTH_SHORT).show();
            uploadDataToServer(context, messageModel);
        }
        else if (intent.getAction().equalsIgnoreCase(SmsReciever.NO_ACTION)) {
            Toast.makeText(context, "no", Toast.LENGTH_SHORT).show();
        }

        NotificationManager nf = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nf.cancel(id);
    }

    public void uploadDataToServer(final Context context, final MessageModel messageModel) {
        if (messageModel != null) {
            if (ConnectivityReceiver.isOnline(context)) {
                String url = PreferenceManager.getDefaultSharedPreferences(context).getString(MainActivity.URL, null);
                if (TextUtils.isEmpty(url)) {
                    Toast.makeText(context, "Please configure the url to send data", Toast.LENGTH_SHORT).show();
                    return;
                }
                Call<String> call = RestClient.get().postSmsData(url, messageModel);
                call.enqueue(new RestCallBack<String>() {
                    @Override
                    public void onSuccess(Call<String> call, Response<String> response) {
                        Toast.makeText(context, "successfully send to server", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int httpCode, String httpMessage) {
                        saveSmsToDatabaseForLaterUse(messageModel);
                    }
                });
            }
            else {
                saveSmsToDatabaseForLaterUse(messageModel);
                Toast.makeText(context, "SMS saved to draft", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void saveSmsToDatabaseForLaterUse(MessageModel messageModel) {
        SQLiteDatabase db = DatabaseHelper.getInstance().getDatabseManipulater();
        Gson gson = new Gson();
        Type type = new TypeToken<MessageModel>() {
        }.getType();
        String json = gson.toJson(messageModel, type);

        ContentValues values = new ContentValues();
        values.put(SMSOfflineDatabase.SMSTable.SMS_DATA, json);
        db.insert(SMSOfflineDatabase.SMSTable.TABLE_NAME, null, values);
    }
}
