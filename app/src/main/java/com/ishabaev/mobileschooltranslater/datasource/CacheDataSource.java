package com.ishabaev.mobileschooltranslater.datasource;

import android.support.annotation.NonNull;

import java.util.List;

import rx.Observable;

public interface CacheDataSource {

    Observable<List<Translation>> getAll(int limit, int offset);

    Observable<List<Translation>> getBookmarks(int limit, int offset);

    Observable<Translation> saveTranslation(@NonNull String text, @NonNull String translation, @NonNull String ui);

    Observable<List<Translation>> saveTranslations(@NonNull String text, @NonNull List<String> translations, @NonNull String ui);

    Observable<Translation> saveBookmark(@NonNull String text, @NonNull String translation, @NonNull String ui);

    Observable<Translation> updateTranslation(@NonNull Translation translation);

    Observable<Translation> getById(long id);

    Observable<List<Translation>> searchTranslation(String text);

    Observable<List<Translation>> searchTranslation(String text, int limit, int offset);

    Observable<List<Translation>> searchBookmark(String text, int limit, int offset);
}
