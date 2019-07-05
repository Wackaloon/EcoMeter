package com.wackalooon.ecometer.di

import android.content.Context
import com.wackalooon.ecometer.EcoMeterApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: EcoMeterApp):Context = application
}
