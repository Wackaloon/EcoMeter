package com.wackalooon.ecometer.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideGson() = Gson()
}
