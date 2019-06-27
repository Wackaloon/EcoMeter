package com.wackalooon.value.domain.usecase

import com.wackalooon.value.domain.repository.ValueRepository

class DeleteValueUseCase(private val valuesRepository: ValueRepository) {
    operator fun invoke(valueId: Long) {
        valuesRepository.deleteValue(valueId)
    }
}
