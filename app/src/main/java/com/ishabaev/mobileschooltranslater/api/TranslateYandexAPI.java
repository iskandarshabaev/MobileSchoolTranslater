package com.ishabaev.mobileschooltranslater.api;


import com.ishabaev.mobileschooltranslater.api.model.TranslateResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface TranslateYandexAPI {

    @GET("translate")
    Observable<TranslateResponse> translate(@Query("text") String text,
                                            @Query("lang") String lang);
}
