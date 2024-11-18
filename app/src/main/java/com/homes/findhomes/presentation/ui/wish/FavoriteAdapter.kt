package com.homes.findhomes.presentation.ui.wish

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.homes.findhomes.R
import com.homes.findhomes.data.model.SearchCompleteResponse
import com.homes.findhomes.databinding.ItemStatisticsKeywordBinding
import com.homes.findhomes.databinding.ItemWishFavoriteBinding
import com.homes.findhomes.presentation.ui.search.ResultRankingAdapter
import com.homes.findhomes.presentation.ui.search.ResultRankingAdapter.OnHeartClickListener

class FavoriteAdapter(val context: Context): ListAdapter<SearchCompleteResponse, FavoriteAdapter.ViewHolder>(DiffCallback()) {
    lateinit var itemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClicked(data: SearchCompleteResponse)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        Log.d("itemClickListener", onItemClickListener.toString())
        itemClickListener = onItemClickListener
    }

    inner class ViewHolder(private val binding: ItemWishFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: SearchCompleteResponse) {
            Glide.with(context)
                .load(item.imgUrl[0])
                .into(binding.ivRanking)
            binding.tvPriceType.text = item.priceType
            binding.tvPrice.text =
                when(item.priceType){
                    "월세" -> "${item.priceForWs}만원"
                    else -> formatPrice(item.price)
                }
            binding.tvDetail1.text = "방 " + item.roomNum + "개"
            binding.tvDetail2.text = "욕실 " + item.washroomNum + "개"
            binding.tvRankingDetail3.text = item.size.toString() + "m"

            binding.clRankingItem.setOnClickListener {
                itemClickListener.onItemClicked(item)
            }
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
        val binding = ItemWishFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))  // ListAdapter에서는 data.size 대신 getItem
    }

    class DiffCallback : DiffUtil.ItemCallback<SearchCompleteResponse>() {
        override fun areItemsTheSame(oldItem: SearchCompleteResponse, newItem: SearchCompleteResponse): Boolean {
            return oldItem.houseId== newItem.houseId  // 새로운 아이템과 이전 아이템을 비교할 수 있는 고윳 값
        }

        override fun areContentsTheSame(oldItem: SearchCompleteResponse, newItem: SearchCompleteResponse): Boolean {
            return oldItem.houseId == newItem.houseId
        }
    }
}
