package com.wackalooon.ecometer.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.wackalooon.ecometer.R
import com.wackalooon.meter.data.repository.MeterRepositoryImpl
import com.wackalooon.meter.data.storage.MeterDatabase
import com.wackalooon.meter.domain.usecase.GetAllMetersUseCase
import com.wackalooon.value.data.repository.ValueRepositoryImpl
import com.wackalooon.value.data.storage.ValueDatabase
import com.wackalooon.value.domain.usecase.GetAllValuesForMeterIdUseCase
import kotlinx.android.synthetic.main.screen_home.*

class HomeFragment : Fragment(), HomeContract.HomeView {

    lateinit var presenter: HomePresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO replace with DI
        loading_progress.hide()
        presenter = HomePresenter(
            GetAllMetersUseCase(MeterRepositoryImpl(MeterDatabase.get(context!!).meterDao())),
            GetAllValuesForMeterIdUseCase(ValueRepositoryImpl(ValueDatabase.get(context!!).valueDao())),
            HomeItemMapper()
        )
        presenter.onViewCreated(this)

    }

    override fun onDestroyView() {
        presenter.onViewDestroyed()
        super.onDestroyView()
    }

    override fun render(state: HomeContract.HomeState) {
        when (state) {
            HomeContract.HomeState.Loanding -> {
                loading_progress.show()
            }
            HomeContract.HomeState.Error -> {
                loading_progress.hide()
            }
            is HomeContract.HomeState.Data -> {
                loading_progress.hide()
                Log.d("METERS", state.meters.toString())
            }
        }
    }
}
