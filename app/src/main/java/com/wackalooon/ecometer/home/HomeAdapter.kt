package com.wackalooon.ecometer.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wackalooon.ecometer.R
import kotlinx.android.synthetic.main.screen_home_item.view.*


class HomeAdapter(
    diffCallback: HomeItemDiffCallback,
    private val listener: (HomeContract.HomeItem) -> Unit
) : ListAdapter<HomeContract.HomeItem, HomeAdapter.ViewHolder>(diffCallback) {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemLayoutView = LayoutInflater.from(parent.context)
                .inflate(R.layout.screen_home_item, parent, false)

        return ViewHolder(itemLayoutView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: HomeContract.HomeItem) {
            itemView.imageView.setImageResource(item.image)
            itemView.item_name.text = item.name
            itemView.item_type.setText(item.type)
            itemView.item_date_value.text = item.date
            itemView.item_value_value.text = item.value
            itemView.setOnClickListener { this@HomeAdapter.listener(getItem(adapterPosition)) }
        }
    }
}
