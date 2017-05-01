package com.alexanderageychenko.ecometer.Tools.dagger2.Module;

import android.support.annotation.NonNull;

import com.alexanderageychenko.ecometer.Model.Entity.rest.Rest;
import com.alexanderageychenko.ecometer.Model.Entity.rest.RestInterface;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Alexander on 01.05.2017.
 */
@Module
public class NetworkModule {

    public final static String NetworkScheduler = "NetworkScheduler";
    private RestInterface restInterface;

    public NetworkModule() {
    }

    public NetworkModule(RestInterface restInterface) {
        this.restInterface = restInterface;
    }

    @Provides
    @Singleton
    @Named(NetworkScheduler)
    Scheduler scheduler() {
        ExecutorService executorService = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                return new Thread(r, "network-scheduler-" + UUID.randomUUID().toString());
            }
        });
        return Schedulers.from(executorService);
    }

    @Provides
    public Rest provideRest() {
        return Rest.getInstance();
    }

    @Provides
    public RestInterface provideRestInterface() {
        return restInterface == null ? Rest.getInstance().getRestInterface() : restInterface;
    }

}
