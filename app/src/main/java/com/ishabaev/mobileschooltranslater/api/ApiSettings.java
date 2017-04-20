package com.ishabaev.mobileschooltranslater.api;


import com.ishabaev.mobileschooltranslater.BuildConfig;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ApiSettings implements Interceptor {

    private ApiSettings(){}

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl url = request.url().newBuilder()
                //.addEncodedPathSegment(BuildConfig.API_VERSION)
                //.addEncodedPathSegment(BuildConfig.API_FORMAT)
                .addQueryParameter("key", BuildConfig.API_KEY)
                .build();
        request = request.newBuilder().url(url).build();
        return chain.proceed(request);
    }

    public static Interceptor create(){
        return new ApiSettings();
    }

}
