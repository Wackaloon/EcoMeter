package com.alexanderageychenko.ecometer.Tools.Navigator;

import java.util.HashMap;

import io.reactivex.Observable;


/**
 * Created by alexanderageychenko on 12.04.17.
 */

public interface Navigator<T> {
    Observable<ScreenTransaction<T>> observableChangeScreen();

    Screen<T> getActiveScreen();

    Screen<T> getFirstStackScreen();

    Screen<T> getActiveScreen(String stack);

    Screen<T> getFirstStackScreen(String stack);

    void openScreen(Screen<T> screen, int animIn, int animOut);

    void openScreenToStack(Screen<T> screen, int animIn, int animOut);

    void saveStack(String stackName);

    void loadStack(String stackName, int animIn, int animOut);

    void loadStack(String stackName, int animIn, int animOut, Screen<T> defaultScreen);

    void createCustomStack(String stackName, Screen<T>... screens);

    void backToStack(int animIn, int animOut);

    void backToStack(int animIn, int animOut, HashMap<String, Object> exParam);

    void changeScreen(Screen<T> screen);

    void reset();

    class Screen<T> {
        public T type;
        public HashMap<String, Object> hashMap;

        public Screen(T type, HashMap<String, Object> hashMap) {
            this.type = type;
            this.hashMap = hashMap;
        }

        public static class HashMapKey {
            public static String entryPoint = "entryPoint";
            public static String activeTab = "activeTab";
        }
    }

    class ScreenTransaction<T> {
        public Screen<T> nowScreen;
        public Screen<T> newScreen;
        public int animIn, animOut;

        public ScreenTransaction(Screen<T> nowScreen, Screen<T> newScreen) {
            this.nowScreen = nowScreen;
            this.newScreen = newScreen;
        }

        public ScreenTransaction(Screen<T> nowScreen, Screen<T> newScreen, int animIn, int animOut) {
            this.nowScreen = nowScreen;
            this.newScreen = newScreen;
            this.animIn = animIn;
            this.animOut = animOut;
        }
    }
}