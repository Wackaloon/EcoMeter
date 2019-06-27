package com.wackalooon.meter.domain.repository

import com.wackalooon.meter.domain.model.Meter
import com.wackalooon.meter.domain.model.Type

interface MeterRepository {
    fun getListOfMeters(): List<Meter>
    fun addMeter(name: String, type: Type)
    fun updateMeter(meter: Meter)
    fun deleteMeter(meterId: Long)
}
