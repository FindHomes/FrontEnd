package com.example.findhomes.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.findhomes.data.County
import com.example.findhomes.databinding.ItemRegionCountyBinding

class RegionCountyAdapter(
    private var counties: List<County>) : RecyclerView.Adapter<RegionCountyAdapter.ViewHolder>(){

    inner class ViewHolder(private val binding: ItemRegionCountyBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: County) {
            binding.tvCounty.text = item.name
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RegionCountyAdapter.ViewHolder {
        val binding = ItemRegionCountyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RegionCountyAdapter.ViewHolder, position: Int) {
        holder.bind(counties[position])
    }

    override fun getItemCount(): Int = counties.size

    fun updateCounties(newCounties: List<County>) {
        counties = newCounties
        notifyDataSetChanged()
    }
}