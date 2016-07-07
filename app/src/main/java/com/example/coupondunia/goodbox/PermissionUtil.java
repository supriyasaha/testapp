package com.example.coupondunia.goodbox;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.text.Html;

public class PermissionUtil {
	/**
	 * Check that all given permissions have been granted by verifying that each entry in the
	 * given array is of the value {@link PackageManager#PERMISSION_GRANTED}.
	 */
	public static boolean verifyPermissions(int[] grantResults) {
		// At least one result must be checked.
		if (grantResults.length < 1) {
			return false;
		}

		// Verify that each required permission has been granted, otherwise return false.
		for (int result : grantResults) {
			if (result != PackageManager.PERMISSION_GRANTED) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks whether the app has the given permissions
	 *
	 * @param permissions
	 * @return
	 */
	public static boolean isPermissionGranted(Context context, String... permissions) {
		if (permissions == null || permissions.length == 0) {
			return false;
		}
		for (String permission : permissions) {
			if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
				return false;
			}
		}
		return true;
	}

	public static AlertDialog.Builder showRequestPermissionDialog(Context context, String body, DialogInterface.OnClickListener positiveClickLinere, DialogInterface.OnClickListener negativeClickListenr) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context)
				.setTitle("Allow GoodBox the following permissions")
				.setMessage(Html.fromHtml(body))
				.setPositiveButton("Allow", positiveClickLinere);
		builder.setCancelable(false);
		if (negativeClickListenr != null)
			builder.setNegativeButton("Deny", negativeClickListenr);
		builder.create().show();
		return builder;
	}

	public static AlertDialog.Builder showMandatoryPermissionDialog(Context context, String body, DialogInterface.OnClickListener positiveClickLinere, DialogInterface.OnClickListener negativeClickListenr) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context)
				.setTitle("Alert")
				.setMessage(Html.fromHtml(body))
				.setPositiveButton("Allow", positiveClickLinere);
		if (negativeClickListenr != null)
			builder.setNegativeButton("Exit App", negativeClickListenr);
		builder.setCancelable(false);
		builder.create().show();
		return builder;
	}
}
