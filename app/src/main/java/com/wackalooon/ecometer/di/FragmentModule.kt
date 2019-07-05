package com.wackalooon.ecometer.di

import com.wackalooon.ecometer.home.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun bindHomeFragment(): HomeFragment
}
