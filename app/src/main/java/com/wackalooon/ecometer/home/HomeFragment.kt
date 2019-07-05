package com.wackalooon.ecometer.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.wackalooon.ecometer.R
import com.wackalooon.ecometer.base.BaseView
import com.wackalooon.ecometer.home.adapter.HomeAdapter
import com.wackalooon.ecometer.home.adapter.HomeItemDiffCallback
import com.wackalooon.ecometer.home.model.HomeEvent
import com.wackalooon.ecometer.home.model.HomeItem
import com.wackalooon.ecometer.home.model.HomeState
import kotlinx.android.synthetic.main.screen_home.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch


class HomeFragment : BaseView<HomeState>() {

    private val viewModel: HomeViewModel by viewModels { viewModelFactory }

    lateinit var adapter: HomeAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        subscribeToChanges()
    }

    override fun onStart() {
        super.onStart()
        viewModel.offerEvent(HomeEvent.LoadMeterDetails)
    }

    private fun subscribeToChanges() = launch {
        viewModel.stateChannel.consumeEach(::render)
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(activity!!)
        items_list.layoutManager = layoutManager
        items_list.isClickable = true

        adapter = HomeAdapter(HomeItemDiffCallback()) { viewModel.offerEvent(HomeEvent.HomeItemClick) }
        items_list.adapter = adapter
    }

    override fun render(state: HomeState) {
        state.apply {
            renderLoadingState(isLoading)
            renderData(data)
            renderError(error)
        }
    }

    private fun renderLoadingState(isLoading: Boolean) {
        if (isLoading) {
            loading_progress.show()
        } else {
            loading_progress.hide()
        }
    }

    private fun renderData(data: List<HomeItem>) {
        adapter.submitList(data)
    }

    private fun renderError(error: String?) {
        if (error.isNullOrEmpty()) {
            return
        }
        Toast.makeText(activity, error, Toast.LENGTH_SHORT).show()
    }
}
