package com.example;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import rx.Observable;

public class Main {

    private static Map<String, Map<String, String>> l = new HashMap<>();

    public static void main(String[] args) {
        ApiFactory.getAPI().getLangs("en")
                .subscribe(result -> handleRepose("en", result), Throwable::printStackTrace);
    }

    private static void handleRepose(String lang, LangsResponse response) {
        addLang(lang, response);
        Set<String> keys = response.langs.keySet();
        String[] k = keys.toArray(new String[keys.size()]);
        Observable.from(k)
                .doOnNext(key -> addLang(key, ApiFactory.getAPI().getLangs(key).toBlocking().first()))
                .subscribe(res->{},e->{}, ()->{
                    try {
                        saveMap();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    private static void addLang(String lang, LangsResponse response) {
        Map<String, String> langs = response.langs;
        l.put(lang, langs);
    }

    private static void saveMap() throws IOException {
        FileOutputStream fos = new FileOutputStream("langs.txt");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(l);
        oos.close();
    }
}
