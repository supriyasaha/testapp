package com.example.coupondunia.goodbox;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;


public class SmsReciever extends BroadcastReceiver {

    public static String YES_ACTION = "com.example.coupondunia.goodbox.yes";
    public static String NO_ACTION = "com.example.coupondunia.goodbox.no";
    MessageModel messageModel;


    final SmsManager sms = SmsManager.getDefault();

    @Override
    public void onReceive(Context context, Intent intent) {
        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();
        String number = PreferenceManager.getDefaultSharedPreferences(context).getString(MainActivity.MESSAGE_NUMBER, null);
        String body = PreferenceManager.getDefaultSharedPreferences(context).getString(MainActivity.MESSAGE_BODY, null);
        if (number != null || body != null) {
            boolean triggerNotif = false;
            try {
                if (bundle != null) {

                    final Object[] pdusObj = (Object[]) bundle.get("pdus");

                    for (int i = 0; i < pdusObj.length; i++) {
                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                        String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                        String message = currentMessage.getDisplayMessageBody();

                        if (phoneNumber != null && number != null && phoneNumber.contains(number)) {
                            triggerNotif = true;
                        }
                        else if (message != null && body != null && message.trim().toLowerCase().contains(body)) {
                            triggerNotif = true;
                        }
                        else {
                            triggerNotif = false;
                            return;
                        }

                        messageModel = new MessageModel();
                        messageModel.number = phoneNumber;
                        messageModel.message = message;
                        messageModel.timestamp = currentMessage.getTimestampMillis();

                        Log.i("SmsReceiver", "senderNum: " + phoneNumber + "; message: " + message);

                        // Show Alert
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context,
                                "senderNum: " + phoneNumber + ", message: " + message, duration);
                        toast.show();
                        generateTriggerNotification(context, messageModel);
                    } // bundle is null
                }
            }
            catch (Exception e) {
                Log.e("SmsReceiver", "Exception smsReceiver" + e);
            }
        }
        else {
            Toast.makeText(context, "Please configure the app", Toast.LENGTH_SHORT).show();
        }
    }


    public static void generateTriggerNotification(Context context, MessageModel messageModel) {
        int NOTIFICATION_ID = 0;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("SMS Number - " + messageModel.number)
                .setContentText("SMS-Body : " + messageModel.message);

        Bundle bundle = new Bundle();
        bundle.putParcelable("messageObject", messageModel);
        bundle.putInt("Notification_id", NOTIFICATION_ID);

        Intent yesReceive = new Intent(context, TriggerActionReceiver.class);
        yesReceive.setAction(YES_ACTION);
        yesReceive.putExtras(bundle);
        PendingIntent pendingIntentYes = PendingIntent.getBroadcast(context, 12345, yesReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(android.R.drawable.ic_input_add, "Yes", pendingIntentYes);


        Intent yesReceive2 = new Intent(context, TriggerActionReceiver.class);
        yesReceive2.setAction(NO_ACTION);
        yesReceive2.putExtras(bundle);
        PendingIntent pendingIntentYes2 = PendingIntent.getBroadcast(context, 12345, yesReceive2, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(android.R.drawable.ic_delete, "No", pendingIntentYes2);


        builder.setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0));
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
