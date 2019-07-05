package com.wackalooon.ecometer.home

import com.wackalooon.ecometer.base.Dispatcher
import com.wackalooon.ecometer.home.model.HomeEvent
import com.wackalooon.ecometer.home.model.HomeItem
import com.wackalooon.ecometer.home.model.HomeItemMapper
import com.wackalooon.ecometer.home.model.HomeUpdate
import com.wackalooon.meter.domain.model.Meter
import com.wackalooon.meter.domain.usecase.GetAllMetersUseCase
import com.wackalooon.value.domain.model.Value
import com.wackalooon.value.domain.usecase.GetAllValuesForMeterIdUseCase
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeDispatcher @Inject constructor(
    private val getAllValuesForMeterIdUseCase: GetAllValuesForMeterIdUseCase,
    private val getAllMetersUseCase: GetAllMetersUseCase,
    private val homeItemsMapper: HomeItemMapper
    // TODO navigator
) : Dispatcher<HomeEvent, HomeUpdate> {

    override fun dispatchEvent(event: HomeEvent) = flow {
        when (event) {
            is HomeEvent.LoadMeterDetails -> {
                emit(HomeUpdate.Loading)
                emit(getHomeItems())
            }
            HomeEvent.HomeItemClick -> {
                // TODO navigate to meters details
            }
        }
    }

    private fun getHomeItems(): HomeUpdate {
        return try {
            val meters = getAllMetersUseCase()
            val items = meters.convertToHomeItems()
            HomeUpdate.Success(items)
        } catch (t: Throwable) {
            HomeUpdate.Failure(t)
        }
    }

    private fun List<Meter>.convertToHomeItems():List<HomeItem>{
        return map { homeItemsMapper.map(it, getAllValuesForMeterIdUseCase(it.id).firstOrNull()) }
    }
}
