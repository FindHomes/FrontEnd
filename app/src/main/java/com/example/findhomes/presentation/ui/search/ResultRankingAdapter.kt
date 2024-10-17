package com.example.findhomes.presentation.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.findhomes.R
import com.example.findhomes.data.model.SearchCompleteResponse
import com.example.findhomes.databinding.ItemResultRankingBinding


class ResultRankingAdapter(private val context: Context) : ListAdapter<SearchCompleteResponse, ResultRankingAdapter.ViewHolder>(
    DiffCallback()
) {
    lateinit var heartClickListener : OnHeartClickListener
    lateinit var itemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClicked(data: SearchCompleteResponse)
    }

    interface OnHeartClickListener {
        fun onHeartClicked(data: SearchCompleteResponse)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        Log.d("itemClickListener", onItemClickListener.toString())
        itemClickListener = onItemClickListener
    }

    fun setOnHeartClickListener(onHeartClickListener: OnHeartClickListener){
        Log.d("heartClickListener", onHeartClickListener.toString())
        heartClickListener = onHeartClickListener
    }

    fun getItemAtPosition(position: Int): SearchCompleteResponse {
        return getItem(position)
    }

    inner class ViewHolder(private val binding: ItemResultRankingBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: SearchCompleteResponse) {
            Glide.with(context)
                .load(item.imgUrl[0])
                .into(binding.ivRanking)
            binding.tvRanking.text = (absoluteAdapterPosition + 1).toString()
            binding.tvRankingPriceType.text = item.priceType
            binding.tvRankingPrice.text =
                when(item.priceType){
                    "월세" -> "${item.priceForWs}만원"
                    else -> formatPrice(item.price)
                }
            binding.tvRankingDetail1.text = "방 " + item.roomNum + "개"
            binding.tvRankingDetail2.text = "욕실 " + item.washroomNum + "개"
            binding.tvRankingDetail3.text = item.size.toString() + "m"
            binding.ivRankingHeart.setImageResource(if(item.favorite) R.drawable.ic_filled_heart else R.drawable.ic_empty_heart)

            binding.cvRanking.setOnClickListener {
                itemClickListener.onItemClicked(item)
            }

            binding.ivRankingHeart.setOnClickListener {
                heartClickListener.onHeartClicked(item)
                item.favorite = !item.favorite
                binding.ivRankingHeart.setImageResource(
                    if (item.favorite) R.drawable.ic_filled_heart else R.drawable.ic_empty_heart
                )
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
        val binding = ItemResultRankingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
