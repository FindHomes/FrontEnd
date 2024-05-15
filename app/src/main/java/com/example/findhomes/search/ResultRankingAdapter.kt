package com.example.findhomes.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.findhomes.data.RankingInfo
import com.example.findhomes.databinding.ItemResultRankingBinding

class ResultRankingAdapter(private val data: ArrayList<RankingInfo>) : RecyclerView.Adapter<ResultRankingAdapter.ViewHolder>(){

    inner class ViewHolder(private val binding: ItemResultRankingBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: RankingInfo) {
            binding.tvRanking.text = item.rank.toString()
            binding.tvRankingPrice.text = item.price.toString()
            binding.tvRankingPriceType.text = item.priceType
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ResultRankingAdapter.ViewHolder {
        val binding = ItemResultRankingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultRankingAdapter.ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
}