package com.example.coupondunia.goodbox;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if(isOnline(context)) {
			// Added a safe check because this receiver gets called multiple times at times in WiFi
//			if(System.currentTimeMillis() - CDSharedPreferenceManager.getInstance().getLastOnlineConnectionTime() >= 2 * 60 * 1000) { // 2 minutes
//				new UploadEventsReceiver().onReceive(context, intent);
//				CDSharedPreferenceManager.getInstance().setLastOnlineConnectionTime(System.currentTimeMillis());
//			}
		}
	}

	public static boolean isOnline(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		//should check null because in air plan mode it will be null
		return (netInfo != null && netInfo.isConnected());
	}
}
