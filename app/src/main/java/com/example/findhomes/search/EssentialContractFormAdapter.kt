package com.example.findhomes.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.findhomes.data.ContractFormData
import com.example.findhomes.databinding.ItemEssentialConditionContractBinding

class EssentialContractFormAdapter(private val data: ArrayList<ContractFormData>) : RecyclerView.Adapter<EssentialContractFormAdapter.ViewHolder>(){

    inner class ViewHolder(private val binding: ItemEssentialConditionContractBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: ContractFormData) {
            binding.tvContractForm.text = item.contractForm
            binding.tvContractPrice.text = item.contractPrice.toString()
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EssentialContractFormAdapter.ViewHolder {
        val binding = ItemEssentialConditionContractBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EssentialContractFormAdapter.ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
}