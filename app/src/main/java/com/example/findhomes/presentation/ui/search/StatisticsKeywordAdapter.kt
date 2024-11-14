package com.example.findhomes.presentation.ui.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.findhomes.databinding.ItemStatisticsKeywordBinding

class StatisticsKeywordAdapter(var onKeywordSelected: (String) -> Unit) :
    ListAdapter<String, StatisticsKeywordAdapter.ViewHolder>(DiffCallback())
{

    var selectedPosition = RecyclerView.NO_POSITION

    inner class ViewHolder(private val binding: ItemStatisticsKeywordBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.statisticsBtnKeyword.setOnClickListener {
                // 이전 선택 위치 저장
                val previousPosition = selectedPosition
                // 새 위치 설정
                selectedPosition = absoluteAdapterPosition
                // 이전 선택된 아이템 업데이트
                if (previousPosition != RecyclerView.NO_POSITION)
                    notifyItemChanged(previousPosition)
                // 새 아이템 업데이트
                notifyItemChanged(selectedPosition)
                // 선택된 키워드를 콜백으로 전달
                onKeywordSelected(getItem(absoluteAdapterPosition))
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: String) {
            binding.statisticsBtnKeyword.text = item
            binding.statisticsBtnKeyword.isSelected = selectedPosition == absoluteAdapterPosition
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
