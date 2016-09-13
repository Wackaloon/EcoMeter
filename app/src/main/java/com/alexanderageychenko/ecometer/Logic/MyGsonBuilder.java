package com.alexanderageychenko.ecometer.Logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by alexanderageychenko on 13.09.16..
 */
public class MyGsonBuilder {
    static public Gson build() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        return gson;
    }
}
