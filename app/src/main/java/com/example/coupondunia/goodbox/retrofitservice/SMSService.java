package com.example.coupondunia.goodbox.retrofitservice;


import com.example.coupondunia.goodbox.MessageModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface SMSService {

    @POST
    Call<String> postSmsData(@Url String url, @Body MessageModel messageModel);
}
