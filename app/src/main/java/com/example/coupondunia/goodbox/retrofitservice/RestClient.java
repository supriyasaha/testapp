package com.example.coupondunia.goodbox.retrofitservice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    public static final int CONNECTION_TIMEOUT = 15;
    public static final int SOCKET_TIMEOUT = 25;
    private static String BASE_URL = "http://requestb.in/";

    private static SMSService quirkService;

    public static Retrofit retrofit;
    public static OkHttpClient okClient;


    static {
        setUp();
    }

    private static void setUp() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        httpClient.readTimeout(SOCKET_TIMEOUT, TimeUnit.SECONDS);

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("User-Agent", "ANDROID")
                        .method(original.method(), original.body())
                        .build();

                Response response = chain.proceed(request);


                return response;
            }
        });

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(loggingInterceptor);

        okClient = httpClient.build();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient.build());


        retrofit = builder.build();
        quirkService = retrofit.create(SMSService.class);
    }

    public static SMSService get() {
        return quirkService;
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static void setBaseUrl(String url) {
        BASE_URL = url;
        setUp();
    }
}
