package com.example.findhomes.domain.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.findhomes.data.DetailPropertyData
import com.example.findhomes.databinding.ItemDetailPropertyBinding

class SearchDetailPropertyAdapter(val data : ArrayList<DetailPropertyData>) : RecyclerView.Adapter<SearchDetailPropertyAdapter.ViewHolder>() {
    inner class ViewHolder(val binding : ItemDetailPropertyBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : DetailPropertyData){
            binding.tvProperty.text = item.property
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemDetailPropertyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
}