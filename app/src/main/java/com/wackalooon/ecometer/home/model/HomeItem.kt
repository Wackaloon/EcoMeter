package com.wackalooon.ecometer.home.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class HomeItem(
    val id: Long,
    @DrawableRes val image: Int,
    val location: String,
    @StringRes val type: Int,
    val value: String,
    val date: String
)
