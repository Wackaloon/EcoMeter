package com.wackalooon.meter.domain.model

sealed class Type {
    data class Water(val waterType: WaterType) : Type()
    object Gas : Type()
    object Electricity : Type()
}
