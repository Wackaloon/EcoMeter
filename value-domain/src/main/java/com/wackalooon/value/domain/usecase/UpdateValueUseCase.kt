package com.wackalooon.value.domain.usecase

import com.wackalooon.value.domain.repository.ValueRepository

class UpdateValueUseCase(private val valuesRepository: ValueRepository) {
    operator fun invoke(valueId: Long, newValue: Double) {
        valuesRepository.updateValue(valueId, newValue)
    }
}
