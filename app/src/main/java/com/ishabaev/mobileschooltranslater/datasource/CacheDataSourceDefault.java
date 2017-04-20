package com.ishabaev.mobileschooltranslater.datasource;

import android.content.Context;
import android.support.annotation.NonNull;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Observable;

public class CacheDataSourceDefault implements CacheDataSource {

    private DaoSession mDaoSession;

    public CacheDataSourceDefault(Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "translations");
        Database db = helper.getWritableDb();
        mDaoSession = new DaoMaster(db).newSession();
    }

    @Override
    public Observable<List<Translation>> searchTranslation(String text) {
        return Observable.fromCallable(() -> searchTranslationByText(text));
    }

    private List<Translation> searchTranslationByText(String text) {
        String search = "%" + text + "%";
        QueryBuilder<Translation> builder = mDaoSession.getTranslationDao().queryBuilder();
        return builder.where(builder.or(TranslationDao.Properties.Text.like(search),
                TranslationDao.Properties.Translation.like(search)))
                .orderDesc(TranslationDao.Properties.Date)
                .list();
    }

    @Override
    public Observable<List<Translation>> getAll(int limit, int offset) {
        return Observable.fromCallable(() -> mDaoSession.getTranslationDao()
                .queryBuilder()
                .orderDesc(TranslationDao.Properties.Date)
                .limit(limit)
                .offset(offset)
                .list());
    }

    @Override
    public Observable<List<Translation>> getBookmarks(int limit, int offset) {
        return Observable.fromCallable(() -> mDaoSession.getTranslationDao()
                .queryBuilder()
                .where(TranslationDao.Properties.Bookmark.eq(true))
                .orderDesc(TranslationDao.Properties.Date)
                .limit(limit)
                .offset(offset)
                .list());
    }

    @Override
    public Observable<Translation> getById(long id) {
        return Observable.fromCallable(() -> mDaoSession.getTranslationDao()
                .queryBuilder()
                .where(TranslationDao.Properties.Id.eq(id))
                .unique());
    }

    @Override
    public Observable<List<Translation>> saveTranslations(@NonNull String text, @NonNull List<String> translations, @NonNull String ui) {
        List<Translation> list = new ArrayList<>();
        for (String translation : translations) {
            list.add(saveTranslation(text, translation, ui, false));
        }
        return Observable.just(list);
    }

    @Override
    public Observable<Translation> saveTranslation(@NonNull String text,
                                                   @NonNull String translation,
                                                   @NonNull String ui) {
        return Observable.fromCallable(() -> saveTranslation(text, translation, ui, false));
    }

    @Override
    public Observable<Translation> saveBookmark(@NonNull String text, @NonNull String translation, @NonNull String ui) {
        return Observable.fromCallable(() -> saveTranslation(text, translation, ui, true));
    }

    private Translation saveTranslation(@NonNull String text,
                                        @NonNull String translation,
                                        @NonNull String ui,
                                        boolean bookmark) {
        Date date = new Date(System.currentTimeMillis());
        Translation entity = new Translation(null, text, translation, ui, bookmark, date);
        mDaoSession.getTranslationDao().insertOrReplace(entity);
        return entity;
    }

    @Override
    public Observable<Translation> updateTranslation(@NonNull Translation translation) {
        return Observable.just(translation)
                .doOnNext(t -> mDaoSession.getTranslationDao().insertOrReplace(t));
    }
}
