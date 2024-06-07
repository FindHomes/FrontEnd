package com.example.findhomes.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.findhomes.data.PreferredRegionData
import com.example.findhomes.databinding.ItemEssentialConditionContractBinding
import com.example.findhomes.databinding.ItemEssentialConditionRegionBinding

class EssentialPreferredRegionAdapter(private val data: ArrayList<PreferredRegionData>) : RecyclerView.Adapter<EssentialPreferredRegionAdapter.ViewHolder>(){

    inner class ViewHolder(private val binding: ItemEssentialConditionRegionBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: PreferredRegionData){
            binding.tvRegionName.text = item.region
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EssentialPreferredRegionAdapter.ViewHolder {
        val binding = ItemEssentialConditionRegionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: EssentialPreferredRegionAdapter.ViewHolder,
        position: Int
    ) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

}