package com.wackalooon.ecometer.base

import androidx.annotation.UiThread
import com.wackalooon.ecometer.di.ViewModelFactory
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

abstract class BaseView<S : State> : DaggerFragment(), CoroutineScope {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override val coroutineContext: CoroutineContext = Job() + Dispatchers.Main

    override fun onDestroyView() {
        super.onDestroyView()
        coroutineContext.cancel()
    }

    @UiThread
    abstract fun render(state: S)
}
