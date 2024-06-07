package com.example.findhomes.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.findhomes.data.PreferredRegionData
import com.example.findhomes.databinding.ItemEssentialPreferredRegionBinding
import com.example.findhomes.databinding.ItemEssentialConditionContractAddBinding

class EssentialPreferredRegionAdapter(
    private val data: ArrayList<PreferredRegionData>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var itemClickListener : OnClickAddListener
    private val TYPE_ITEM = 0
    private val TYPE_ADD = 1

    interface OnClickAddListener {
        fun onClickAdd()
    }

    fun setOnItemClickListener(onItemClickListener: OnClickAddListener){
        itemClickListener = onItemClickListener
    }

    inner class ItemViewHolder(private val binding: ItemEssentialPreferredRegionBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: PreferredRegionData) {
            binding.tvContractForm.text = item.region
        }
    }

    inner class AddViewHolder(private val binding: ItemEssentialConditionContractAddBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.clEssential2.setOnClickListener {
                itemClickListener.onClickAdd()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == data.size) TYPE_ADD else TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_ADD) {
            val binding = ItemEssentialConditionContractAddBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            AddViewHolder(binding)
        } else {
            val binding = ItemEssentialPreferredRegionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ItemViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.bind(data[position])
        }
    }

    override fun getItemCount(): Int = data.size
}
