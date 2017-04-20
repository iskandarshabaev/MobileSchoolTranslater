package com.ishabaev.mobileschooltranslater.screen.translate;

import android.support.annotation.NonNull;

import com.ishabaev.mobileschooltranslater.datasource.Translation;

import java.util.List;

public interface TranslateView {

    void showText(@NonNull String text);

    void showTranslation(@NonNull String text);

    void showLangs(String lang1, String lang2);

    void showHistory(List<Translation> translations);

    void addHistoryItemToTop(Translation translation);
}
