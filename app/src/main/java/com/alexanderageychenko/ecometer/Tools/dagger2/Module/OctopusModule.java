package com.alexanderageychenko.ecometer.Tools.dagger2.Module;

import android.support.annotation.NonNull;

import com.alexanderageychenko.ecometer.Octopus.home.HomeOctopus;
import com.alexanderageychenko.ecometer.Octopus.home.IHomeOctopus;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alexander on 16.04.2017.
 */
@Module
public class OctopusModule {
    public static final String EXECUTOR_NAME = "Presenter";

    @Singleton
    @Provides
    @Named(EXECUTOR_NAME)
    public ExecutorService providePresenterExecutor() {
        return Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                return new Thread(r, "Presenter:" + UUID.randomUUID().toString());
            }
        });
    }
    @Provides
    @Singleton
    IHomeOctopus provideIHomeOctopus(){
        return new HomeOctopus();
    }
}
