package com.ishabaev.mobileschooltranslater.datasource;

import android.content.Context;
import android.support.annotation.NonNull;

public class DataSourceProvider {

    private static TranslateDataSource sTranslateDataSource;
    private static CommonDataSource sCommonDataSource;
    private static CacheDataSource sCacheDataSource;

    public static TranslateDataSource provideTranslate(Context context) {
        if (sTranslateDataSource == null) {
            CacheDataSource cacheDataSource = provideCache(context);
            sTranslateDataSource = new TranslateDataSourceNetwork(cacheDataSource);
        }
        return sTranslateDataSource;
    }

    public static CommonDataSource provideCommon(@NonNull Context context) {
        if (sCommonDataSource == null) {
            sCommonDataSource = new CommonDataSourceStorage(context);
        }
        return sCommonDataSource;
    }

    public static CacheDataSource provideCache(@NonNull Context context) {
        if (sCacheDataSource == null) {
            sCacheDataSource = new CacheDataSourceDefault(context);
        }
        return sCacheDataSource;
    }
}
