package com.example.findhomes.search

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.findhomes.databinding.ItemSelectMonthlyBinding

class ContractPriceAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val typeMonthly = 0
    private val typeDeposit = 1
    private val typeTrading = 2

    inner class ItemMonthlyViewHolder(val binding : ItemSelectMonthlyBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}