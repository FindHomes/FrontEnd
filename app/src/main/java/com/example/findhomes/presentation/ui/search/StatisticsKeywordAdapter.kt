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
import com.example.findhomes.data.model.SearchStatisticsResponse
import com.example.findhomes.databinding.ItemStatisticsInfoBinding
import com.example.findhomes.databinding.ItemStatisticsKeywordBinding
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry


class StatisticsKeywordAdapter(var onKeywordSelected: (String) -> Unit) :
    ListAdapter<String, StatisticsKeywordAdapter.ViewHolder>(DiffCallback())
{
    inner class ViewHolder(private val binding: ItemStatisticsKeywordBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                // 현재 ViewHolder 위치의 아이템을 가져와 콜백 실행
                onKeywordSelected(getItem(absoluteAdapterPosition))
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: String) {
            binding.statisticsBtnKeyword.text = item
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStatisticsKeywordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))  // ListAdapter에서는 data.size 대신 getItem
    }

    class DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem== newItem  // 새로운 아이템과 이전 아이템을 비교할 수 있는 고윳 값
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}
