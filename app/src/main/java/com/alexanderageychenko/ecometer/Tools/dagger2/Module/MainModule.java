package com.alexanderageychenko.ecometer.Tools.dagger2.Module;

import android.app.Application;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {
    private Type type;
    private Application application;

    public MainModule(Type type, Application application) {
        this.type = type;
        this.application = application;
    }

    public MainModule(Type type) {
        this.type = type;
    }

    public MainModule() {
        this.type = Type.ANDROID;
    }

    @Provides
    Type provideType() {
        return type;
    }

    @Provides
    Application provideApplication(){
        return application;
    }

    public enum Type {
        TEST, ANDROID
    }
}
