package com.wackalooon.ecometer.home.model

import android.text.format.DateFormat
import com.wackalooon.ecometer.R
import com.wackalooon.meter.domain.model.Meter
import com.wackalooon.meter.domain.model.Type
import com.wackalooon.meter.domain.model.WaterType
import com.wackalooon.value.domain.model.Value
import java.util.*


class HomeItemMapper {
    fun map(meter: Meter, value: Value?): HomeItem {
        return HomeItem(
            meter.id,
            mapImage(meter.type),
            meter.location,
            mapType(meter.type),
            value?.value?.toString() ?: "0",
            mapDate(value?.creationTime)
        )
    }

    private fun mapType(type: Type): Int {
        return when (type) {
            is Type.Water -> {
                when (type.waterType) {
                    WaterType.HOT -> R.string.home_item_water_type_hot
                    WaterType.COLD -> R.string.home_item_water_type_cold
                }
            }
            Type.Gas -> R.string.home_item_type_gas
            Type.Electricity -> R.string.home_item_type_electricity
        }
    }

    private fun mapImage(type: Type): Int {
        return when (type) {
            is Type.Water -> {
                when (type.waterType) {
                    WaterType.HOT -> R.drawable.ic_home_black_24dp
                    WaterType.COLD -> R.drawable.ic_home_black_24dp
                }
            }
            Type.Gas -> R.drawable.ic_home_black_24dp
            Type.Electricity -> R.drawable.ic_home_black_24dp
        }
    }

    private fun mapDate(timestamp: Long?): String {
        if (timestamp == null) {
            return ""
        }

        val cal = Calendar.getInstance()
        cal.timeInMillis = timestamp
        return DateFormat.format(calculateFormat(cal), cal).toString()
    }

    private fun calculateFormat(calendar: Calendar): String {
        val present = Calendar.getInstance()
        return if (present.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
            "dd MMM hh:mm"
        } else {
            "dd MMM yyyy hh:mm"
        }
    }
}
