package com.example;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface LangAPI {

    @GET("getLangs")
    Observable<LangsResponse> getLangs(@Query("ui") String ui);
}
