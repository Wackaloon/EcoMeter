package com.wackalooon.ecometer.home

import com.wackalooon.ecometer.R
import com.wackalooon.meter.domain.model.Meter
import com.wackalooon.value.domain.model.Value

class HomeItemMapper {
    fun map(meter: Meter, value: Value?): HomeContract.HomeItem {
        return HomeContract.HomeItem(
            R.drawable.ic_home_black_24dp,
            meter.name,
            value?.value?.toString() ?: "0.0",
            value?.creationTime?.toString() ?: "Never"
        )
    }
}
