package com.wackalooon.ecometer.di

import android.content.Context
import com.wackalooon.meter.data.storage.MeterDao
import com.wackalooon.meter.data.storage.MeterDatabase
import com.wackalooon.value.data.storage.ValueDao
import com.wackalooon.value.data.storage.ValueDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DaoModule {

    @Provides
    @Singleton
    fun provideMetersDao(context: Context): MeterDao = MeterDatabase.get(context).meterDao()

    @Provides
    @Singleton
    fun provideValuesDao(context: Context): ValueDao = ValueDatabase.get(context).valueDao()
}
