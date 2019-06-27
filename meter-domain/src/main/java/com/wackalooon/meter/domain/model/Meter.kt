package com.wackalooon.meter.domain.model

typealias ValueId = Long

data class Meter(
    val id: Long,
    val name: String,
    val type: Type,
    val values: ArrayList<ValueId> = ArrayList()
)
