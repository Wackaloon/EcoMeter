package com.wackalooon.ecometer.home

import androidx.lifecycle.liveData
import com.wackalooon.ecometer.base.Action
import com.wackalooon.ecometer.home.model.HomeItemMapper
import com.wackalooon.meter.domain.model.Meter
import com.wackalooon.meter.domain.usecase.GetAllMetersUseCase
import com.wackalooon.value.domain.model.Value
import com.wackalooon.value.domain.usecase.GetAllValuesForMeterIdUseCase
import javax.inject.Inject

class HomeDispatcher @Inject constructor(
    private val getAllValuesForMeterIdUseCase: GetAllValuesForMeterIdUseCase,
    private val getAllMetersUseCase: GetAllMetersUseCase,
    private val homeItemsMapper: HomeItemMapper
) {

    fun dispatchAction(action: Action) = liveData {
        when (action) {
            is HomeContract.HomeAction.LoadMeterDetails -> {
                emit(HomeContract.HomeResult.Loading)
                emit(getHomeItems())
            }
        }
    }

    private suspend fun getHomeItems(): HomeContract.HomeResult {
        return try {
            val meters = getAllMetersUseCase()
            val items = meters.map { homeItemsMapper.map(it, getValueForMeter(it)) }
            HomeContract.HomeResult.Success(items)
        } catch (t: Throwable) {
            HomeContract.HomeResult.Failure(t)
        }
    }

    private fun getValueForMeter(meter: Meter): Value? {
        return getAllValuesForMeterIdUseCase(meter.id).firstOrNull()
    }
}
