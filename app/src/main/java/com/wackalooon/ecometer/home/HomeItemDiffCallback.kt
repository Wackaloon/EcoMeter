package com.wackalooon.ecometer.home

import androidx.recyclerview.widget.DiffUtil

class HomeItemDiffCallback : DiffUtil.ItemCallback<HomeContract.HomeItem>() {
    override fun areItemsTheSame(oldItem: HomeContract.HomeItem, newItem: HomeContract.HomeItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: HomeContract.HomeItem, newItem: HomeContract.HomeItem): Boolean {
        return oldItem.value == newItem.value
                && oldItem.date == newItem.date
    }
}
