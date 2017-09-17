package com.alexanderageychenko.ecometer.Tools.Navigator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;


/**
 * Created by alexanderageychenko on 12.04.17.
 */
public abstract class NavigatorImpl<T> implements Navigator<T> {
    private final PublishSubject<ScreenTransaction<T>> publishSubjectChangeScreen = PublishSubject.create();
    private final ArrayList<Screen<T>> activeStack = new ArrayList<>();
    private final HashMap<String, ArrayList<Screen<T>>> savedStack = new HashMap<>();

    @Override
    public Observable<ScreenTransaction<T>> observableChangeScreen() {
        return publishSubjectChangeScreen;
    }

    @Override
    public synchronized Screen<T> getActiveScreen() {
        if (activeStack.isEmpty()) return new Screen<T>(getDefaultType(), null);
        return activeStack.get(activeStack.size() - 1);
    }

    @Override
    public synchronized Screen<T> getFirstStackScreen() {
        if(activeStack.isEmpty())return new Screen<>(getDefaultType(), null);
        Screen screen = activeStack.get(0);
        if (screen == null) screen = new Screen<T>(getDefaultType(), null);
        return screen;
    }

    @Override
    public synchronized void openScreen(Screen<T> screen, int animIn, int animOut) {
        Screen<T> nowScreen = new Screen<T>(getDefaultType(), null);
        if (!activeStack.isEmpty())
            nowScreen = activeStack.get(activeStack.size() - 1);
        ScreenTransaction<T> screenTransaction = new ScreenTransaction<T>(nowScreen, screen, animIn, animOut);
        activeStack.clear();
        activeStack.add(screen);
        publishSubjectChangeScreen.onNext(screenTransaction);
    }

    @Override
    public synchronized void openScreenToStack(Screen<T> screen, int animIn, int animOut) {
        Screen<T> activeScreen = new Screen<T>(getDefaultType(), null);
        if (!activeStack.isEmpty()) activeScreen = activeStack.get(activeStack.size() - 1);
        if (activeScreen.type == screen.type) return;
        ScreenTransaction<T> screenTransaction = new ScreenTransaction<T>(activeScreen, screen, animIn, animOut);
        activeStack.add(screen);
        publishSubjectChangeScreen.onNext(screenTransaction);
    }

    @Override
    public synchronized void saveStack(String stackName) {
        savedStack.put(stackName, new ArrayList<>(activeStack));
    }

    @Override
    public void loadStack(String stackName, int animIn, int animOut) {
        loadStack(stackName, animIn, animOut, new Screen<T>(getDefaultType(), null));
    }

    @Override
    public synchronized void loadStack(String stackName, int animIn, int animOut, Screen<T> defaultScreen) {
        ArrayList<Screen<T>> newStack = new ArrayList<>();
        if (!savedStack.containsKey(stackName)) {
            newStack.add(defaultScreen);
        } else {
            newStack.addAll(savedStack.get(stackName));
        }
        Screen<T> nowScreen = new Screen<>(getDefaultType(), null);
        if(!activeStack.isEmpty()) nowScreen = activeStack.get(activeStack.size() - 1);
        ScreenTransaction<T> screenTransaction = new ScreenTransaction<T>(nowScreen, newStack.get(newStack.size() - 1), animIn, animOut);
        activeStack.clear();
        activeStack.addAll(newStack);
        publishSubjectChangeScreen.onNext(screenTransaction);
    }

    @Override
    public synchronized void createCustomStack(String stackName, Screen<T>... screens) {
        ArrayList<Screen<T>> newStack = new ArrayList<>();
        Collections.addAll(newStack, screens);
        savedStack.put(stackName, newStack);
    }

    @Override
    public void backToStack(int animIn, int animOut) {
        backToStack(animIn, animOut, null);
    }

    @Override
    public synchronized void backToStack(int animIn, int animOut, HashMap<String, Object> param) {
        if (activeStack.isEmpty()) return;
        if (activeStack.size() == 1) {
            ScreenTransaction<T> screenTransaction = new ScreenTransaction<T>(activeStack.get(activeStack.size() - 1), new Screen<T>(getDefaultType(), null), animIn, animOut);
            activeStack.clear();
            publishSubjectChangeScreen.onNext(screenTransaction);
            return;
        }
        Screen<T> nowScreen = activeStack.get(activeStack.size() - 1);
        Screen<T> newScreen = activeStack.get(activeStack.size() - 2);
        if (param != null) {
            if (newScreen.hashMap != null) {
                newScreen.hashMap.putAll(param);
            } else {
                newScreen.hashMap = param;
            }
        }
        ScreenTransaction<T> screenTransaction = new ScreenTransaction<T>(nowScreen, newScreen, animIn, animOut);
        activeStack.remove(activeStack.size() - 1);
        publishSubjectChangeScreen.onNext(screenTransaction);
    }

    @Override
    public void changeScreen(Screen screen) {
        if (activeStack.isEmpty()) return;
        activeStack.set(activeStack.size() - 1, screen);
    }

    @Override
    public void reset() {
        activeStack.clear();
        savedStack.clear();
    }

    @Override
    public Screen<T> getActiveScreen(String stackName) {
        if (savedStack.containsKey(stackName)) {
            ArrayList<Screen<T>> stack = savedStack.get(stackName);
            return stack.isEmpty()
                    ? new Screen<T>(getDefaultType(), null)
                    : stack.get(0);
        } else {
            return new Screen<T>(getDefaultType(), null);
        }
    }

    @Override
    public Screen<T> getFirstStackScreen(String stackName) {
        if (savedStack.containsKey(stackName)) {
            ArrayList<Screen<T>> stack = savedStack.get(stackName);
            return stack.isEmpty()
                    ? new Screen<T>(getDefaultType(), null)
                    : stack.get(0);
        } else {
            return new Screen<T>(getDefaultType(), null);
        }
    }

    protected abstract T getDefaultType();
}