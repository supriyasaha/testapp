package com.example.coupondunia.goodbox;

import android.app.Application;
import android.content.Context;

import com.parse.Parse;


public class GoodBoxApplication extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        Parse.enableLocalDatastore(this);
//        ParseObject.create("GameScore");
        Parse.initialize(this,"j2ArDjONUHSkiDmhRd9M1YdCgudcQKeoWXEGFIyo","kw3iHhMnUH2BAiuqOBvjWLLbhKaWivA1bKf17SBC");
    }
}
