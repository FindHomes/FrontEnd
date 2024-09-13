package com.example.findhomes.presentation.ui.search

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.findhomes.R
import com.example.findhomes.data.model.City
import com.example.findhomes.data.model.County
import com.example.findhomes.databinding.ItemRegionCityBinding

class RegionCityAdapter(
    private val data: List<City>,
    private val onCitySelected: (City) -> Unit
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
                if(previousPosition != absoluteAdapterPosition){
                    onCitySelected(item)
                    selectedPosition = absoluteAdapterPosition
                    notifyItemChanged(previousPosition)
                    notifyItemChanged(selectedPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRegionCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val isSelected = position == selectedPosition
        holder.bind(data[position], isSelected)
    }

    override fun getItemCount(): Int = data.size
}
