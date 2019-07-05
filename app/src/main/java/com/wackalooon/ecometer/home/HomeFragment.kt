package com.wackalooon.ecometer.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.wackalooon.ecometer.R
import com.wackalooon.ecometer.base.BaseView
import com.wackalooon.ecometer.home.adapter.HomeAdapter
import com.wackalooon.ecometer.home.adapter.HomeItemDiffCallback
import kotlinx.android.synthetic.main.screen_home.*


class HomeFragment : BaseView() {

    private val viewModel: HomeViewModel by viewModels { viewModelFactory }

    lateinit var adapter: HomeAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO replace with DI
        loading_progress.hide()

        setupRecyclerView()

        viewModel.homeState.observe(this) {
            render(it)
        }
    }

    private fun setupRecyclerView() {

        val layoutManager = LinearLayoutManager(activity!!)
        items_list.layoutManager = layoutManager
        items_list.isClickable = true

        adapter = HomeAdapter(HomeItemDiffCallback(), {})
        items_list.adapter = adapter
    }

    private fun render(state: HomeContract.HomeViewState) {
        if (state.isLoading) {
            loading_progress.show()
        } else {
            loading_progress.hide()
        }
        adapter.submitList(state.data)

        if (state.error != null) {
            Toast.makeText(activity, state.error, Toast.LENGTH_SHORT).show()
        }
    }
}
