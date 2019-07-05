package com.wackalooon.ecometer.home

import com.wackalooon.meter.domain.usecase.GetAllMetersUseCase
import com.wackalooon.value.domain.usecase.GetAllValuesForMeterIdUseCase
import kotlinx.coroutines.*

class HomePresenter(
    private val getAllMeters: GetAllMetersUseCase,
    private val getAllValuesForMeterIdUseCase: GetAllValuesForMeterIdUseCase,
    private val mapper: HomeItemMapper
) : HomeContract.HomePresenter {
    override fun onMeterClick(meterId: Long) {
        // TODO add navigation to meter details
    }

    private var view: HomeContract.HomeView? = null
    private var job: Job = Job()

    override fun onViewCreated(view: HomeContract.HomeView) {
        this.view = view
        loadData(view)
    }

    override fun onViewDestroyed() {
        this.view = null
        job.cancel()
    }

    override fun onBackButtonPressed() {
        // TODO add navigation
    }

    private fun loadData(view: HomeContract.HomeView) {
        job.cancel()
        job = GlobalScope.launch {
            val items = getAllMeters().map {
                val value = getAllValuesForMeterIdUseCase(it.id).firstOrNull()
                mapper.map(it, value)
            }

            withContext(Dispatchers.Main) {
                view.render(HomeContract.HomeState.Data(items))
            }
        }
    }
}
