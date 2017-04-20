package com.example;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ApiSettings implements Interceptor {

    private ApiSettings() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl url = request.url().newBuilder()
                .addQueryParameter("key", "trnsl.1.1.20170318T170638Z.6102b0f539b7aa71.15fe198c9e27c004212ec23f71edd79bbd3c45b6")
                .build();
        request = request.newBuilder().url(url).build();
        return chain.proceed(request);
    }

    public static Interceptor create() {
        return new ApiSettings();
    }

}
