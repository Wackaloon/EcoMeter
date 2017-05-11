package com.alexanderageychenko.ecometer.Tools.dagger2.Module;

import android.support.annotation.NonNull;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Alexander on 11.05.2017.
 */

public class AppRxSchedulers {
    public static final String PRESENTER_THREAD = "PresenterThread";
    public static final String UI_THREAD = "UIThread";
    public static final String NETWORK_THREAD = "NetworkThread";
    public static final String COMPUTATION_THREAD = "NetworkThread";
    public static final String IO_THREAD = "IOThread";

    @Singleton
    @Provides
    @Named(PRESENTER_THREAD)
    public Scheduler providePresenterExecutor() {
        return Schedulers.from(Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                return new Thread(r, "Presenter:" + UUID.randomUUID().toString());
            }
        }));
    }

    @Singleton
    @Provides
    @Named(UI_THREAD)
    public Scheduler androidUI() {
        return AndroidSchedulers.mainThread();
    }

    @Singleton
    @Provides
    @Named(IO_THREAD)
    public Scheduler io() {
        return Schedulers.io();
    }

    @Singleton
    @Provides
    @Named(COMPUTATION_THREAD)
    public Scheduler computation() {
        return Schedulers.computation();
    }

    @Singleton
    @Provides
    @Named(NETWORK_THREAD)
    public Scheduler network() {
        return Schedulers.from(Executors.newFixedThreadPool(4, new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                return new Thread(r, "Network:" + UUID.randomUUID().toString());
            }
        }));
    }


}
