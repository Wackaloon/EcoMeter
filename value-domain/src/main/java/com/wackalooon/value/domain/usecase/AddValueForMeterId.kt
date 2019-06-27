package com.wackalooon.value.domain.usecase

import com.wackalooon.value.domain.model.Value
import com.wackalooon.value.domain.repository.ValueRepository
import java.util.*
import kotlin.random.Random

class AddValueForMeterId(private val valuesRepository: ValueRepository) {
    operator fun invoke(value: Double, meterId: Long) {
        val id = Random.nextLong()
        val timestamp = Date().time
        val newValue = Value(id, timestamp, value)
        valuesRepository.addValueForMeterId(newValue, meterId)
    }
}
