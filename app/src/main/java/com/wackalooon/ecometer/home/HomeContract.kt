package com.wackalooon.ecometer.home

import com.wackalooon.ecometer.base.BasePresenter
import com.wackalooon.ecometer.base.BaseView
import com.wackalooon.meter.domain.model.Meter

interface HomeContract {

    interface HomePresenter : BasePresenter<HomeView> {
        fun onMeterClick(meterId: Long)
    }

    interface HomeView : BaseView {
        fun render(state: HomeState)
    }

    sealed class HomeState {
        object Loanding : HomeState()
        object Error : HomeState()
        data class Data(val meters: List<HomeItem>) : HomeState()
    }

    data class HomeItem(val image: Int, val name:String, val value:String, val date:String)
}
