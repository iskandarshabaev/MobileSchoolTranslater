package com.ishabaev.mobileschooltranslater.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonHolder {

    public static Gson sGson;

    public static void init(){
        sGson = new GsonBuilder().create();
    }
}
