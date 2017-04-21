package com.ishabaev.mobileschooltranslater;

import android.app.Application;

import com.ishabaev.mobileschooltranslater.datasource.DataSourceProvider;
import com.ishabaev.mobileschooltranslater.util.GsonHolder;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        GsonHolder.init();

        //Инициализация общего источника данных
        DataSourceProvider.provideCommon(this);
        /*for (int i = 0; i < 3000; i++) {
            DataSourceProvider.provideCache(this)
                    .saveTranslation("текст" + i, "text" + i, "ru-en")
                    .subscribe();
        }*/
    }
}
