package com.wackalooon.ecometer.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.wackalooon.ecometer.base.BaseViewModel
import com.wackalooon.ecometer.home.HomeContract.HomeResult.*
import javax.inject.Inject

class HomeViewModel @Inject constructor(dispatcher: HomeDispatcher) : BaseViewModel() {

    private val viewState = HomeContract.HomeViewState()

    val homeState: LiveData<HomeContract.HomeViewState> =
        Transformations.map(dispatcher.dispatchAction(HomeContract.HomeAction.LoadMeterDetails)) {
            when (it) {
                is Loading -> viewState.loading()
                is Success -> viewState.data(it.data)
                is Failure -> viewState.error(it.error)
            }
        }

}
