package com.wackalooon.ecometer.di

import com.wackalooon.ecometer.EcoMeterApp
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ActivityModule::class,
        AppModule::class,
        DaoModule::class,
        MapperModule::class,
        NetworkModule::class,
        RepositoryModule::class,
        UseCaseModule::class,
        ViewModelModule::class,
        AndroidSupportInjectionModule::class
    ]
)
interface AppComponent : AndroidInjector<EcoMeterApp> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<EcoMeterApp>()
}
