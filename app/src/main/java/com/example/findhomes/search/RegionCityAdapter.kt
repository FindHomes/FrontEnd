package com.example.findhomes.search

import android.graphics.Color
import android.provider.CalendarContract.Colors
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.findhomes.R
import com.example.findhomes.data.City
import com.example.findhomes.data.County
import com.example.findhomes.databinding.ItemRegionCityBinding
import com.example.findhomes.databinding.ItemResultRankingBinding

class RegionCityAdapter(
    private val data: List<City>,
    private val onCitySelected: (List<County>) -> Unit
) : RecyclerView.Adapter<RegionCityAdapter.ViewHolder>() {
    private var selectedPosition = 0


    inner class ViewHolder(private val binding: ItemRegionCityBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: City, isSelected : Boolean) {
            binding.tvCity.text = item.name
            binding.clTextCity.setBackgroundColor(
                if(isSelected) Color.WHITE
                else ContextCompat.getColor(binding.root.context, R.color.sub_2)
            )
            itemView.setOnClickListener {
                val previousPosition = selectedPosition
                if(previousPosition != adapterPosition){
                    onCitySelected(item.counties)
                    selectedPosition = adapterPosition
                    notifyItemChanged(previousPosition)
                    notifyItemChanged(selectedPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionCityAdapter.ViewHolder {
        val binding = ItemRegionCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RegionCityAdapter.ViewHolder, position: Int) {
        val isSelected = position == selectedPosition
        holder.bind(data[position], isSelected)
    }

    override fun getItemCount(): Int = data.size
}
