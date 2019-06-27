package com.wackalooon.value.domain.repository

import com.wackalooon.value.domain.model.Value

interface ValueRepository {
    fun getValuesForMeterId(meterId: Long): List<Value>
    fun addValueForMeterId(value: Value, meterId: Long)
    fun updateValue(valueId: Long, newValue: Double)
    fun deleteValue(valueId: Long)
}
