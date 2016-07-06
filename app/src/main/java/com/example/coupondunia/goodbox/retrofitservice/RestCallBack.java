package com.example.coupondunia.goodbox.retrofitservice;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class RestCallBack<T> implements Callback<T> {

    public static int UNAUTHORISED = 400;
    public static int SOMETHING_WENT_WRONG = 401;
    public static int INVALID_CREDENTIALS = 402;
    public static int INVALID_MERCHANT = 403;
    public static int NO_DATA = 404;
    public static int INVALID_CARD = 405;
    public static int INACTIVE_CARD = 406;
    public static int SUCCESS = 200;


    public abstract void onSuccess(Call<T> call, Response<T> response);

    public abstract void onFailure(int httpCode, String httpMessage);

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            onSuccess(call, response);
        }
        else {
            onFailure(response.code(), response.message());
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onFailure(SOMETHING_WENT_WRONG, t.getMessage());
    }
}
