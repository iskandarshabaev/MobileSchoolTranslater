package com.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ApiFactory {

    private static final Gson GSON_DATE = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    private static OkHttpClient sClient;

    private static LangAPI api;

    public static LangAPI getAPI() {
        LangAPI service = api;
        if (service == null) {
            synchronized (ApiFactory.class) {
                service = api;
                if (service == null) {
                    service = api = createAPI();
                }
            }
        }
        return service;
    }

    private static LangAPI createAPI() {
        return new Retrofit.Builder()
                .baseUrl("https://translate.yandex.net/api/v1.5/tr.json/")
                .client(getClient())
                .addConverterFactory(GsonConverterFactory.create(GSON_DATE))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(LangAPI.class);
    }

    private static OkHttpClient getClient() {
        OkHttpClient client = sClient;
        if (client == null) {
            synchronized (ApiFactory.class) {
                client = sClient;
                if (client == null) {
                    client = sClient = buildClient();
                }
            }
        }
        return client;
    }

    private static OkHttpClient buildClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(ApiSettings.create())
                .addInterceptor(ApiSettings.create())
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                .build();
    }
}
