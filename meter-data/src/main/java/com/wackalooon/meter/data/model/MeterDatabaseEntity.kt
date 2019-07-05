package com.wackalooon.meter.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wackalooon.meter.domain.model.Type

@Entity
data class MeterDatabaseEntity (
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val type: Type
)
