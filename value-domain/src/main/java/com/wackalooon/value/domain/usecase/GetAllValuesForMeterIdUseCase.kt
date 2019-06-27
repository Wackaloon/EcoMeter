package com.wackalooon.value.domain.usecase

import com.wackalooon.value.domain.model.Value
import com.wackalooon.value.domain.repository.ValueRepository

class GetAllValuesForMeterIdUseCase(private val valuesRepository: ValueRepository) {
    operator fun invoke(meterId: Long): List<Value> {
        return valuesRepository.getValuesForMeterId(meterId)
    }
}
