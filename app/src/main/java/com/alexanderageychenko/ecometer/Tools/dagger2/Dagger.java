package com.alexanderageychenko.ecometer.Tools.dagger2;

/**
 * Created by Alexander on 15.04.2017.
 */

public class Dagger {
    static AppComponent appComponent;

    public static void setAppComponent(AppComponent app) {
        appComponent = app;
    }

    public static AppComponent get() {
        return appComponent;
    }
}
