package com.example.coupondunia.goodbox.database;


import android.provider.BaseColumns;

public class SMSOfflineDatabase {

    public SMSOfflineDatabase() {
    }

    public static class SMSTable implements BaseColumns {

        public static final String TABLE_NAME = "sms";
        public static final String ENTRY_ID = "_id";
        public static final String SMS_DATA = "data";
    }
}
