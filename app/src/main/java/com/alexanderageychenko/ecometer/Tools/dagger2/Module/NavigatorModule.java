package com.alexanderageychenko.ecometer.Tools.dagger2.Module;

import com.alexanderageychenko.ecometer.Tools.Navigator.MainNavigator;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by alexanderageychenko on 9/17/17.
 */

@Module
public class NavigatorModule {

    @Singleton
    @Provides
    MainNavigator provideHomeNavigator() {
        return new MainNavigator();
    }
}
