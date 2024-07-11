package com.example.findhomes.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.findhomes.data.RankingInfo
import com.example.findhomes.data.SearchResultData
import com.example.findhomes.databinding.ItemResultRankingBinding

class RegionCityAdapter(private val data: ArrayList<SearchResultData>, val context: Context) : RecyclerView.Adapter<RegionCityAdapter.ViewHolder>(){

    inner class ViewHolder(private val binding: ItemResultRankingBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: SearchResultData) {
            Glide.with(context)
                .load(item.img)
                .into(binding.ivRanking)
            binding.tvRanking.text = item.ranking.toString()
            binding.tvRankingPrice.text = item.price
            binding.tvRankingPriceType.text = item.priceType
            binding.tvRankingDetail1.text = item.room
            binding.tvRankingDetail2.text = item.etc
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RegionCityAdapter.ViewHolder {
        val binding = ItemResultRankingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RegionCityAdapter.ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
}