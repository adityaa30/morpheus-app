package com.morpheus.aditya.drive.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.morpheus.aditya.drive.DriveUtils;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class ApiService {

    private static DriveApi mService;

    private static void ApiService() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60 ,TimeUnit.SECONDS)
                .readTimeout(60 ,TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DriveUtils.API_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        mService = retrofit.create(DriveApi.class);
    }

    public static DriveApi getService() {
        if (mService == null) {
            ApiService();
            return mService;
        }
        return mService;
    }

}
