package com.wackalooon.meter.domain.usecase

import com.wackalooon.meter.domain.model.Meter
import com.wackalooon.meter.domain.repository.MeterRepository

class GetAllMetersUseCase(private val metersRepository: MeterRepository) {
    operator fun invoke(): List<Meter> {
        return metersRepository.getListOfMeters()
    }
}
