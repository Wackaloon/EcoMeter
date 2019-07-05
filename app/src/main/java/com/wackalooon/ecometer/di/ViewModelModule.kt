package com.wackalooon.ecometer.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wackalooon.ecometer.home.HomeViewModel
import com.wackalooon.ecometer.home.di.ViewModelFactory
import com.wackalooon.ecometer.home.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun movieViewModel(viewModel: HomeViewModel): ViewModel

}
