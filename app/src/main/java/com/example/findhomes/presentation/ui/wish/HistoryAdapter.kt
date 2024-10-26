package com.example.findhomes.presentation.ui.wish

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.findhomes.data.model.WishHistoryResponse
import com.example.findhomes.databinding.ItemWishHistoryBinding

class HistoryAdapter(): ListAdapter<WishHistoryResponse, HistoryAdapter.ViewHolder>(DiffCallback()) {
    lateinit var itemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClicked(data: WishHistoryResponse)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        Log.d("itemClickListener", onItemClickListener.toString())
        itemClickListener = onItemClickListener
    }

    inner class ViewHolder(private val binding: ItemWishHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: WishHistoryResponse) {
            binding.tvHistoryTypeInfo.text = item.typeInfo
            binding.tvHistoryPriceInfo.text = item.priceInfo
            binding.tvHistoryRegionInfo.text = item.regionInfo
            binding.tvHistoryKeyword.text = item.keyword
        }
    }

    private fun formatPrice(price: Int): String {
        return when {
            price >= 10000 -> {
                val billions = price / 10000
                val remainder = price % 10000
                if (remainder == 0) "${billions}억"
                else "${billions}억 ${remainder}만원"
            }
            else -> "${price}만원"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWishHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<WishHistoryResponse>() {
        override fun areItemsTheSame(oldItem: WishHistoryResponse, newItem: WishHistoryResponse): Boolean {
            return oldItem.searchLogId== newItem.searchLogId
        }

        override fun areContentsTheSame(oldItem: WishHistoryResponse, newItem: WishHistoryResponse): Boolean {
            return oldItem.searchLogId == newItem.searchLogId
        }
    }
}
