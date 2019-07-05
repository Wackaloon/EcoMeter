package com.wackalooon.ecometer.di

import com.wackalooon.meter.domain.repository.MeterRepository
import com.wackalooon.meter.domain.usecase.GetAllMetersUseCase
import com.wackalooon.value.domain.repository.ValueRepository
import com.wackalooon.value.domain.usecase.GetAllValuesForMeterIdUseCase
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
class UseCaseModule {
    @Provides
    @Reusable
    fun getAllMetersUseCase(meterRepository: MeterRepository) = GetAllMetersUseCase(meterRepository)

    @Provides
    @Reusable
    fun getAllValuesForMeterIdUseCase(valuesRepository: ValueRepository) = GetAllValuesForMeterIdUseCase(valuesRepository)
}
