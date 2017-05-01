package com.alexanderageychenko.ecometer.Tools.dagger2.Module;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class UIModule {
    Context context;

    public UIModule(Context context) {
        this.context = context;
    }

    @Provides
    Context provideContext() {
        return context;
    }

}
