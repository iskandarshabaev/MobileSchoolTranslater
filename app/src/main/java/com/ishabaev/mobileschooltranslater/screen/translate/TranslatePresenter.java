package com.ishabaev.mobileschooltranslater.screen.translate;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.ishabaev.mobileschooltranslater.datasource.CacheDataSource;
import com.ishabaev.mobileschooltranslater.datasource.CommonDataSource;
import com.ishabaev.mobileschooltranslater.datasource.TranslateDataSource;
import com.ishabaev.mobileschooltranslater.datasource.Translation;
import com.ishabaev.mobileschooltranslater.screen.BasePresenter;
import com.ishabaev.mobileschooltranslater.util.RxSchedulers;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

public class TranslatePresenter extends BasePresenter {

    private static final int CHUNK = 15;
    private TranslateView mView;
    private TranslateDataSource mTranslateDataSource;
    private CommonDataSource mCommonDataSource;
    private CacheDataSource mCacheDataSource;
    private Map.Entry<String, String> langA;
    private Map.Entry<String, String> langB;
    private String systemLang = "en";
    private int offset = 0;

    public TranslatePresenter(@NonNull TranslateView view,
                              @NonNull TranslateDataSource translateDataSource,
                              @NonNull CommonDataSource commonDataSource,
                              @NonNull CacheDataSource cacheDataSource) {
        mView = view;
        mTranslateDataSource = translateDataSource;
        mCommonDataSource = commonDataSource;
        mCacheDataSource = cacheDataSource;
    }

    public void setSystemLang(String lang) {
        systemLang = lang;
    }

    public void loadTranslation(long id) {
        mCacheDataSource.getById(id)
                .compose(RxSchedulers.async())
                .subscribe(this::handleTranslation, this::showError);
    }

    private void handleTranslation(Translation translation) {
        String[] parts = splitUI(translation.getUi());
        String langAkey = parts[0];
        String langBkey = parts[1];
        String langAValue = mCommonDataSource.getLang(langAkey, systemLang);
        String langBValue = mCommonDataSource.getLang(langBkey, systemLang);
        langA = new AbstractMap.SimpleEntry<>(langAkey, langAValue);
        langB = new AbstractMap.SimpleEntry<>(langBkey, langBValue);
        mView.showLangs(langAValue, langBValue);
        mView.showText(translation.getText());
        mView.showTranslation(translation.getTranslation());
    }

    public void translateText(@NonNull String text) {
        if (text.length() == 0) {
            return;
        }
        mTranslateDataSource.translateText(text, makeUI(langA.getKey(), langB.getKey()))
                .compose(RxSchedulers.async())
                .subscribe(this::handleTranslations, this::showError);
    }

    public void loadLangs(@NonNull String onLang) {
        langA = mCommonDataSource.getLangA(onLang);
        langB = mCommonDataSource.getLangB(onLang);
        mView.showLangs(langA.getValue(), langB.getValue());
    }

    public void swapLanguages() {
        Map.Entry<String, String> swap = langA;
        langA = langB;
        langB = swap;
        mCommonDataSource.saveLangA(langA.getKey());
        mCommonDataSource.saveLangB(langB.getKey());
        mView.showLangs(langA.getValue(), langB.getValue());
    }

    private void handleTranslations(@NonNull List<Translation> translations) {
        if (translations.size() > 0) {
            mView.showTranslation(translations.get(0).getTranslation());
            mView.addHistoryItemToTop(translations.get(0));
        }
    }

    public void addToFavorites(String text, String translation) {
        if (TextUtils.isEmpty(text) || TextUtils.isEmpty(translation)) {
            return;
        }
        String ui = makeUI(langA.getKey(), langB.getKey());
        mCacheDataSource.saveBookmark(text, translation, ui)
                .compose(RxSchedulers.async())
                .subscribe(r -> {
                }, this::showError);
    }

    private String makeUI(String keyA, String keyB) {
        return keyA + "-" + keyB;
    }

    private String[] splitUI(String ui) {
        return ui.split("-");
    }

    private void showError(Throwable e) {
        e.printStackTrace();
    }

    public void loadHistory() {
        mCacheDataSource.getAll(CHUNK, offset)
                .doOnNext(r -> offset += CHUNK)
                .compose(RxSchedulers.async())
                .subscribe(mView::showHistory, this::showError);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }
}
