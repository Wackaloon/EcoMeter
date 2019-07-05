package com.wackalooon.ecometer.home.model

import com.wackalooon.ecometer.base.State

data class HomeState(
    val isLoading: Boolean = false,
    val data: List<HomeItem> = emptyList(),
    val error: String? = null
) : State {

    fun loading(): HomeState {
        return copy(isLoading = true)
    }

    fun data(data: List<HomeItem>): HomeState {
        return copy(isLoading = false, data = data, error = null)
    }

    fun error(error: Throwable): HomeState {
        return copy(isLoading = false, error = error.localizedMessage)
    }
}
