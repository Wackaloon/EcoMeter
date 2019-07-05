package com.wackalooon.ecometer.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wackalooon.ecometer.home.di.ViewModelFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseView : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }


}
