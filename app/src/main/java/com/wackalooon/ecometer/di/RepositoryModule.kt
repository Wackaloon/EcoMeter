package com.wackalooon.ecometer.di

import com.wackalooon.meter.data.repository.MeterRepositoryImpl
import com.wackalooon.meter.data.storage.MeterDao
import com.wackalooon.meter.domain.repository.MeterRepository
import com.wackalooon.value.data.repository.ValueRepositoryImpl
import com.wackalooon.value.data.storage.ValueDao
import com.wackalooon.value.domain.repository.ValueRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideMovieRepository(dao: MeterDao): MeterRepository {
        return MeterRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideValueRepository(dao: ValueDao): ValueRepository {
        return ValueRepositoryImpl(dao)
    }
}
