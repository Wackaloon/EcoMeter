package com.wackalooon.ecometer.di

import com.wackalooon.ecometer.home.model.HomeItemMapper
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
class MapperModule {

    @Provides
    @Reusable
    fun getHomeItemMapper() = HomeItemMapper()
}
