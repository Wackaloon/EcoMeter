package com.wackalooon.ecometer.home

import com.wackalooon.ecometer.base.Action
import com.wackalooon.ecometer.base.Result
import com.wackalooon.ecometer.base.ViewState
import com.wackalooon.ecometer.home.model.HomeItem

interface HomeContract {

    sealed class HomeResult : Result {
        object Loading : HomeResult()
        data class Success(val data: List<HomeItem>) : HomeResult()
        data class Failure(val error: Throwable) : HomeResult()
    }

    sealed class HomeAction:Action {
        object OpenMeterDetails : HomeAction()
        object LoadMeterDetails : HomeAction()
    }

    data class HomeViewState(
        val isLoading: Boolean = false,
        val data: List<HomeItem> = emptyList(),
        val error: String? = null
    ) : ViewState {

        fun loading(): HomeViewState{
            return copy(isLoading = true)
        }

        fun data(data: List<HomeItem>): HomeViewState{
            return copy(isLoading = false, data = data, error = null)
        }

        fun error(error: Throwable): HomeViewState{
            return copy(isLoading = false, error = error.localizedMessage)
        }
    }
}
