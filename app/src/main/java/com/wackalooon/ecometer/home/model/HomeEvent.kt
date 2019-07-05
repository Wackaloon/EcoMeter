package com.wackalooon.ecometer.home.model

import com.wackalooon.ecometer.base.Event

sealed class HomeEvent : Event {
    object HomeItemClick : HomeEvent()
    object LoadMeterDetails : HomeEvent()
}
