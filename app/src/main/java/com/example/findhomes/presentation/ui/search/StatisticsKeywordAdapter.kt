package com.example.findhomes.presentation.ui.search

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.findhomes.data.model.GraphDataResponse
import com.example.findhomes.data.model.SearchCompleteResponse
import com.example.findhomes.databinding.ItemStatisticsInfoBinding
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry


class StatisticsKeywordAdapter() : ListAdapter<GraphDataResponse, StatisticsKeywordAdapter.ViewHolder>(
    DiffCallback()
) {
    inner class ViewHolder(private val binding: ItemStatisticsInfoBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: GraphDataResponse) {
            binding.statisticsTvData1.text = item.dataName
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStatisticsInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))  // ListAdapter에서는 data.size 대신 getItem
    }

    class DiffCallback : DiffUtil.ItemCallback<GraphDataResponse>() {
        override fun areItemsTheSame(oldItem: GraphDataResponse, newItem: GraphDataResponse): Boolean {
            return oldItem.dataName== newItem.dataName  // 새로운 아이템과 이전 아이템을 비교할 수 있는 고윳 값
        }

        override fun areContentsTheSame(oldItem: GraphDataResponse, newItem: GraphDataResponse): Boolean {
            return oldItem.dataName == newItem.dataName
        }
    }
}
