package com.example.coupondunia.goodbox;

import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText messageNumber, messageBodyText;
    String msgNum, msgBody;

    public static String MESSAGE_NUMBER = "number", MESSAGE_BODY = "body";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageNumber = (EditText) findViewById(R.id.messageNumber);
        messageBodyText = (EditText) findViewById(R.id.messageBody);


        ((Button) findViewById(R.id.btSubmit)).setOnClickListener(this);
        ((Button) findViewById(R.id.btCancel)).setOnClickListener(this);


        msgNum = PreferenceManager.getDefaultSharedPreferences(this).getString(MESSAGE_NUMBER, null);
        msgBody = PreferenceManager.getDefaultSharedPreferences(this).getString(MESSAGE_BODY, null);
        if (!TextUtils.isEmpty(msgNum)) {
            messageNumber.setText(msgNum);
        }

        if (!TextUtils.isEmpty(msgBody)) {
            messageBodyText.setText(msgBody);
        }

//        MessageModel messageModel = new MessageModel();
//        messageModel.number = "8879730199";
//        messageModel.message = "hfvwiuejfbwejf";
//        messageModel.timestamp = System.currentTimeMillis();
//        SmsReciever.generateTriggerNotification(this, messageModel);

    }

    public void saveTriggerDetailsToSharedPreference() {
        msgNum = TextUtils.isEmpty(messageNumber.getText()) ? null : messageNumber.getText().toString();
        msgBody = TextUtils.isEmpty(messageBodyText.getText()) ? null : messageBodyText.getText().toString().trim().toLowerCase();

        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(MESSAGE_NUMBER, msgNum).commit();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(MESSAGE_BODY, msgBody).commit();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btSubmit:
                saveTriggerDetailsToSharedPreference();
                break;

            case R.id.btCancel:
                break;
        }
    }
}
