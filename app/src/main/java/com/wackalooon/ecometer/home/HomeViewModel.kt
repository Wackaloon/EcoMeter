package com.wackalooon.ecometer.home

import com.wackalooon.ecometer.base.BaseViewModel
import com.wackalooon.ecometer.home.model.HomeEvent
import com.wackalooon.ecometer.home.model.HomeState
import com.wackalooon.ecometer.home.model.HomeUpdate
import com.wackalooon.ecometer.home.model.HomeUpdate.*
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    dispatcher: HomeDispatcher
) : BaseViewModel<HomeEvent, HomeUpdate, HomeState>(HomeState(), dispatcher) {

    override fun updateState(update: HomeUpdate): HomeState {
        return when (update) {
            is Loading -> currentState.loading()
            is Success -> currentState.data(update.data)
            is Failure -> currentState.error(update.error)
        }
    }
}
