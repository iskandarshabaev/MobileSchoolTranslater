package com.ishabaev.mobileschooltranslater.datasource;

import android.support.annotation.NonNull;

import com.ishabaev.mobileschooltranslater.api.ApiFactory;

import java.util.List;

import rx.Observable;


public class TranslateDataSourceNetwork implements TranslateDataSource {

    private CacheDataSource mCacheDataSource;

    public TranslateDataSourceNetwork(CacheDataSource cacheDataSource) {
        mCacheDataSource = cacheDataSource;
    }

    @Override
    public Observable<List<Translation>> translateText(@NonNull String text, String lang) {
        return ApiFactory.getAPI().translate(text, lang)
                .flatMap(response -> mCacheDataSource.saveTranslations(text, response.text, lang));
    }
}