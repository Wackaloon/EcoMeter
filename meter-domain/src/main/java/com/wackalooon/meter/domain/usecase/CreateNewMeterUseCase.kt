package com.wackalooon.meter.domain.usecase

import com.wackalooon.meter.domain.model.Type
import com.wackalooon.meter.domain.repository.MeterRepository

class CreateNewMeterUseCase(private val metersRepository: MeterRepository) {
    operator fun invoke(name: String, type: Type) {
        metersRepository.addMeter(name, type)
    }
}
