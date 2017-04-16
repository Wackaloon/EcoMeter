package com.alexanderageychenko.ecometer.Tools.dagger2.Module;

import com.alexanderageychenko.ecometer.Octopus.home.HomeOctopus;
import com.alexanderageychenko.ecometer.Octopus.home.IHomeOctopus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alexander on 16.04.2017.
 */
@Module
public class OctopusModule {
    @Provides
    @Singleton
    IHomeOctopus provideIHomeOctopus(){
        return new HomeOctopus();
    }
}
