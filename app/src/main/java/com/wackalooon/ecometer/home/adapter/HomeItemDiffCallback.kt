package com.wackalooon.ecometer.home.adapter

import androidx.recyclerview.widget.DiffUtil
import com.wackalooon.ecometer.home.HomeContract
import com.wackalooon.ecometer.home.model.HomeItem

class HomeItemDiffCallback : DiffUtil.ItemCallback<HomeItem>() {
    override fun areItemsTheSame(oldItem: HomeItem, newItem: HomeItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: HomeItem, newItem: HomeItem): Boolean {
        return oldItem.value == newItem.value
                && oldItem.date == newItem.date
    }
}
