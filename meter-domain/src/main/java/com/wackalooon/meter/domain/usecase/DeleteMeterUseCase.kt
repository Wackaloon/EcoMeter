package com.wackalooon.meter.domain.usecase

import com.wackalooon.meter.domain.repository.MeterRepository

class DeleteMeterUseCase(private val metersRepository: MeterRepository) {
    operator fun invoke(meterId: Long) {
        metersRepository.deleteMeter(meterId)
    }
}
