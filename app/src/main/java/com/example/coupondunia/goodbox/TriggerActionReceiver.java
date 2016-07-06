package com.example.coupondunia.goodbox;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import com.example.coupondunia.goodbox.database.DatabaseHelper;
import com.example.coupondunia.goodbox.database.SMSOfflineDatabase;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;


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

    public void uploadDataToServer(final Context context, MessageModel messageModel) {
        if (messageModel != null) {
            if (ConnectivityReceiver.isOnline(context)) {
                getMessageRequestBody(messageModel).saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast.makeText(context, "Save to our server", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                saveSmsToDatabaseForLaterUse();
            }
        }
    }

    public ParseObject getMessageRequestBody(MessageModel messageModel) {
        ParseObject gameScore = new ParseObject("GameScore");
        gameScore.put("number", messageModel.number);
        gameScore.put("messageBody", messageModel.message);
        gameScore.put("timestamp", messageModel.timestamp);
        return gameScore;
    }

    public void saveSmsToDatabaseForLaterUse() {
        SQLiteDatabase db = DatabaseHelper.getInstance().getDatabseManipulater();

        ContentValues values = new ContentValues();
        values.put(SMSOfflineDatabase.SMSTable.SMS_DATA, "");
        db.insert(SMSOfflineDatabase.SMSTable.TABLE_NAME, null, values);
    }
}
