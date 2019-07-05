package com.wackalooon.ecometer.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.wackalooon.ecometer.base.BasePresenter
import com.wackalooon.ecometer.base.BaseView

interface HomeContract {

    interface HomePresenter : BasePresenter<HomeView> {
        fun onHomeItemClick(item: HomeItem)
    }

    interface HomeView : BaseView {
        fun render(state: HomeState)
    }

    sealed class HomeState {
        object Loanding : HomeState()
        object Error : HomeState()
        data class Data(val meters: List<HomeItem>) : HomeState()
    }

    data class HomeItem(
        val id: Long,
        @DrawableRes val image: Int,
        val name: String,
        @StringRes val type: Int,
        val value: String,
        val date: String
    )
}
