package com.QuetzalSoft.myapplication.operationsRetrofitApi;

import com.QuetzalSoft.myapplication.Utils.Constant;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClicent {


    private Retrofit retrofit;
    private static ApiClicent instance;

    public ApiClicent() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static synchronized ApiClicent getInstance() {
        if (instance == null) {
            instance = new ApiClicent();
        }
        return instance;
    }

    public ApiInterface getApi() {
        return retrofit.create(ApiInterface.class);
    }
}
