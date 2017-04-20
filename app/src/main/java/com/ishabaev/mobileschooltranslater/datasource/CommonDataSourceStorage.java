package com.ishabaev.mobileschooltranslater.datasource;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommonDataSourceStorage implements CommonDataSource {

    private static String LANGS_FILE_NAME = "langs";

    private Map<String, Map<String, String>> langsBase;

    private SharedPreferences mSharedPreferences;
    public static String LANG_A = "lang_a";
    public static String LANG_B = "lang_b";

    public CommonDataSourceStorage(@NonNull Context context) {
        mSharedPreferences = context.getSharedPreferences("MobileSchoolTranslater",
                Context.MODE_PRIVATE);
        initLangDictionary(context);
    }

    private void initLangDictionary(@NonNull Context context) {
        try {
            InputStream is = context.getAssets().open(LANGS_FILE_NAME);
            ObjectInputStream ois = new ObjectInputStream(is);
            Object object = ois.readObject();
            langsBase = (Map<String, Map<String, String>>) object;
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getLang(String key, String onLang) {
        return langsBase.get(onLang).get(key);
    }

    @Override
    public List<Map.Entry<String, String>> getLangs(String onLang) {
        return new ArrayList<>(langsBase.get(onLang).entrySet());
    }

    @Override
    public void saveLangA(String langAKey) {
        mSharedPreferences.edit().putString(LANG_A, langAKey).apply();
    }

    @Override
    public Map.Entry<String, String> getLangA(String onLang) {
        String langKey = mSharedPreferences.getString(LANG_A, "en");
        String langValue = langsBase.get(onLang).get(langKey);
        return new AbstractMap.SimpleEntry<>(langKey, langValue);
    }

    @Override
    public void saveLangB(String langBKey) {
        mSharedPreferences.edit().putString(LANG_B, langBKey).apply();
    }

    @Override
    public Map.Entry<String, String> getLangB(String onLang) {
        String langKey = mSharedPreferences.getString(LANG_B, "en");
        String langValue = langsBase.get(onLang).get(langKey);
        return new AbstractMap.SimpleEntry<>(langKey, langValue);
    }
}
