package com.wackalooon.ecometer.base

interface BasePresenter<T : BaseView> {
    fun onViewCreated(view: T)
    fun onViewDestroyed()
    fun onBackButtonPressed()
}
