package com.example.findhomes.presentation.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.findhomes.R
import com.example.findhomes.data.model.County
import com.example.findhomes.databinding.ItemRegionCountyBinding

class RegionCountyAdapter(var counties: List<County>) : RecyclerView.Adapter<RegionCountyAdapter.ViewHolder>(){

    private lateinit var countyClickListener: OnCountyClickListener
    var selectedPosition = RecyclerView.NO_POSITION

    interface OnCountyClickListener{
        fun onCountyClicked(data : County)
    }

    fun setOnCountyClickListener(onCountyClickListener : OnCountyClickListener){
        countyClickListener = onCountyClickListener
    }

    inner class ViewHolder(private val binding: ItemRegionCountyBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: County, isSelected: Boolean) {
            binding.tvCounty.text = item.name
            binding.tvCounty.setTextColor(
                if(isSelected) ContextCompat.getColor(binding.root.context, R.color.button_color)
                else ContextCompat.getColor(binding.root.context, R.color.body_3)
            )
            binding.tvCounty.setOnClickListener {
                val previousPosition = selectedPosition
                if(previousPosition != adapterPosition){
                    countyClickListener.onCountyClicked(item)
                    selectedPosition = adapterPosition
                    notifyItemChanged(previousPosition)
                    notifyItemChanged(selectedPosition)
                }
            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemRegionCountyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val isSelected = position == selectedPosition
        holder.bind(counties[position], isSelected)
    }

    override fun getItemCount(): Int = counties.size

    fun updateCounties(newCounties: List<County>) {
        counties = newCounties
        selectedPosition = RecyclerView.NO_POSITION
        notifyDataSetChanged()
    }
}