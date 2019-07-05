package com.wackalooon.ecometer.di

import com.wackalooon.ecometer.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun provideMainActivity(): MainActivity
}
