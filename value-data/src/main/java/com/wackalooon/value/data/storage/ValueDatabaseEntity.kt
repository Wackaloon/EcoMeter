package com.wackalooon.value.data.storage

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ValueDatabaseEntity (
    @PrimaryKey(autoGenerate = true) val id: Long,
    val creationTime: Long,
    val meterId: Long,
    val value: Double
)
