package com.example.findhomes.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.findhomes.data.City
import com.example.findhomes.data.County
import com.example.findhomes.databinding.ItemResultRankingBinding

class RegionCityAdapter(
    private val data: List<City>,
    private val onCitySelected: (List<County>) -> Unit
) : RecyclerView.Adapter<RegionCityAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemResultRankingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: City) {
            binding.tvRanking.text = item.name
            itemView.setOnClickListener { onCitySelected(item.counties) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionCityAdapter.ViewHolder {
        val binding = ItemResultRankingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RegionCityAdapter.ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
}
