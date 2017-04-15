package com.alexanderageychenko.ecometer.Logic.dagger2;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Alexander on 15.04.2017.
 */
@Singleton
@Component(modules={DepositoryModule.class, AppModule.class})
public interface AppComponent {
    Getter getGetter();
    Injector getInjector();
}
