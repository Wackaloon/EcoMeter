package com.wackalooon.meter.domain.model

data class Meter(
    val id: Long,
    val name: String,
    val location: String,
    val type: Type
)
