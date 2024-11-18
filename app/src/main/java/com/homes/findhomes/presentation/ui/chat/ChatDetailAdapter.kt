package com.homes.findhomes.presentation.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.homes.findhomes.databinding.ItemChatSendBinding
import com.homes.findhomes.databinding.ItemChatReceiveBinding

class ChatDetailAdapter(private var messages: MutableList<ChatData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    companion object {
        const val VIEW_TYPE_SEND = 0
        const val VIEW_TYPE_RECEIVE = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isReceived) VIEW_TYPE_RECEIVE else VIEW_TYPE_SEND
    }

    inner class SendViewHolder(val binding: ItemChatSendBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatData) {
            binding.tvChatMessage.text = item.message
        }
    }

    inner class ReceiveViewHolder(val binding: ItemChatReceiveBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatData) {
            binding.tvChatMessage.text = item.message
            if (item.isLoading) {
                binding.laChatLoading.visibility = View.VISIBLE
                binding.laChatLoading.playAnimation()
                binding.tvChatMessage.visibility = View.GONE
            } else {
                binding.laChatLoading.visibility = View.GONE
                binding.laChatLoading.pauseAnimation()
                binding.tvChatMessage.visibility = View.VISIBLE
                binding.tvChatMessage.text = item.message
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_SEND -> SendViewHolder(ItemChatSendBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            VIEW_TYPE_RECEIVE -> ReceiveViewHolder(ItemChatReceiveBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SendViewHolder -> holder.bind(messages[position])
            is ReceiveViewHolder -> holder.bind(messages[position])
        }
    }

    override fun getItemCount(): Int = messages.size
}
