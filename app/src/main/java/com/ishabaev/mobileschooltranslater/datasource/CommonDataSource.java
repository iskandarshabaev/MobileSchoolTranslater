package com.ishabaev.mobileschooltranslater.datasource;

import java.util.List;
import java.util.Map;

public interface CommonDataSource {

    String getLang(String key, String onLang);

    List<Map.Entry<String, String>> getLangs(String onLang);

    void saveLangA(String langAKey);

    Map.Entry<String, String> getLangA(String onLang);

    void saveLangB(String langBKey);

    Map.Entry<String, String> getLangB(String onLang);

}
