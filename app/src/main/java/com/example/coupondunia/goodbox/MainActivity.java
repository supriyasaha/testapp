package com.example.coupondunia.goodbox;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText messageNumber, messageBodyText, urlEditText;
    String msgNum, msgBody, urlBody;
    public static int READ_SMS = 101;

    public static String MESSAGE_NUMBER = "number", MESSAGE_BODY = "body", URL = "url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageNumber = (EditText) findViewById(R.id.messageNumber);
        messageBodyText = (EditText) findViewById(R.id.messageBody);
        urlEditText = (EditText) findViewById(R.id.urlText);


        ((Button) findViewById(R.id.btSubmit)).setOnClickListener(this);
        ((Button) findViewById(R.id.btCancel)).setOnClickListener(this);


        msgNum = PreferenceManager.getDefaultSharedPreferences(this).getString(MESSAGE_NUMBER, null);
        msgBody = PreferenceManager.getDefaultSharedPreferences(this).getString(MESSAGE_BODY, null);
        urlBody = PreferenceManager.getDefaultSharedPreferences(this).getString(URL, null);

        if (!TextUtils.isEmpty(msgNum)) {
            messageNumber.setText(msgNum);
        }

        if (!TextUtils.isEmpty(msgBody)) {
            messageBodyText.setText(msgBody);
        }

        if (!TextUtils.isEmpty(urlBody)) {
            urlEditText.setText(urlBody);
        }
//        MessageModel messageModel = new MessageModel();
//        messageModel.number = "8879730199";
//        messageModel.message = "hfvwiuejfbwejf";
//        messageModel.timestamp = System.currentTimeMillis();
//        SmsReciever.generateTriggerNotification(this, messageModel);

        getPermission();
    }

    public void saveTriggerDetailsToSharedPreference() {
        msgNum = TextUtils.isEmpty(messageNumber.getText()) ? null : messageNumber.getText().toString();
        msgBody = TextUtils.isEmpty(messageBodyText.getText()) ? null : messageBodyText.getText().toString().trim().toLowerCase();
        urlBody = TextUtils.isDigitsOnly(urlEditText.getText()) ? null : urlEditText.getText().toString().trim();

        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(MESSAGE_NUMBER, msgNum).commit();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(MESSAGE_BODY, msgBody).commit();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(URL, urlBody).commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btSubmit:
                saveTriggerDetailsToSharedPreference();
                Toast.makeText(this, "user details saved", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btCancel:
                break;
        }
    }

    private void getPermission() {
        String message = "";
        final List<Integer> permissionGr = new ArrayList<>();

        if (!PermissionUtil.isPermissionGranted(this, Manifest.permission.READ_SMS)) {
            permissionGr.add(READ_SMS);
            message += "<p></p><b>SMS</b><p>" + getString(R.string.permission_read_message) + "</p>";
        }

        if (permissionGr.size() > 0) {
            showPermissionDialog(message);
        }
    }

    private void showPermissionDialog(final String message) {
        PermissionUtil.showRequestPermissionDialog(this, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {//allow
                getPermission(READ_SMS, Manifest.permission.READ_SMS);
            }
        }, new DialogInterface.OnClickListener() {//cancel
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showMandatoryPermissionDialog(message);
            }
        });
    }

    public void getPermission(int phonePermission, String... permissionGroups) {
        String p[] = new String[permissionGroups.length];
        int i = 0;
        for (String perms : permissionGroups) {
            p[i++] = perms;
        }
        ActivityCompat.requestPermissions(this, p, phonePermission);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PermissionUtil.isPermissionGranted(this, Manifest.permission.READ_SMS)) {
//            onCaptureBillImage();
        }
        else {
            showMandatoryPermissionDialog("This permission is necessary for the app");
        }
    }

    private void showMandatoryPermissionDialog(String alertText) {
        PermissionUtil.showMandatoryPermissionDialog(this, alertText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {//allow
                getPermission(READ_SMS, Manifest.permission.READ_SMS);
            }
        }, new DialogInterface.OnClickListener() {//cancel
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.finish();//exit the app
            }
        });
    }

}
