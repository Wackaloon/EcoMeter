package com.wackalooon.ecometer.home.model

import com.wackalooon.ecometer.base.Update

sealed class HomeUpdate : Update {
    object Loading : HomeUpdate()
    data class Success(val data: List<HomeItem>) : HomeUpdate()
    data class Failure(val error: Throwable) : HomeUpdate()
}
