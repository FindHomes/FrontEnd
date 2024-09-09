package com.example.findhomes.presentation.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.findhomes.data.model.HousesResponse
import com.example.findhomes.databinding.ItemChatRecommendBinding
import com.example.findhomes.databinding.ItemResultRankingBinding


class ChatRecommendAdapter() : ListAdapter<String, ChatRecommendAdapter.ViewHolder>(
    DiffCallback()
) {
    lateinit var itemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClicked(data: String)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        itemClickListener = onItemClickListener
    }

    inner class ViewHolder(private val binding: ItemChatRecommendBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: String) {
            binding.tvChatRecommend.text = item

            binding.clChatRecommend.setOnClickListener {
                itemClickListener.onItemClicked(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemChatRecommendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}
