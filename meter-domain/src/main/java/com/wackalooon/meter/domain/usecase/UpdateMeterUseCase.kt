package com.wackalooon.meter.domain.usecase

import com.wackalooon.meter.domain.model.Meter
import com.wackalooon.meter.domain.repository.MeterRepository

class UpdateMeterUseCase(private val metersRepository: MeterRepository) {
    operator fun invoke(meter: Meter) {
        metersRepository.updateMeter(meter)
    }
}
