package com.ishabaev.mobileschooltranslater.datasource;

import android.support.annotation.NonNull;

import com.google.gson.internal.LinkedTreeMap;

import java.util.List;
import java.util.Map;

import rx.Observable;

public interface TranslateDataSource {

    /**
     * Перевод текста
     *
     * @param text - текст который необходимо превести
     * @param lang - Направление перевода(прим. en-ru)
     */
    Observable<List<Translation>> translateText(@NonNull String text, String lang);
}
