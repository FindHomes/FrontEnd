package com.example.findhomes.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.findhomes.databinding.ItemChatSendBinding
import com.example.findhomes.databinding.ItemChatReceiveBinding
import com.example.findhomes.data.ChatData
import com.example.findhomes.databinding.ItemChatEndBinding

class ChatDetailAdapter(private var messages: MutableList<ChatData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var yesClickListener: OnYesClickListener

    companion object {
        const val VIEW_TYPE_SEND = 0
        const val VIEW_TYPE_RECEIVE = 1
        const val VIEW_TYPE_END = 2
    }

    interface OnYesClickListener{
        fun onYesClicked()
    }

    fun setYesClickListener(onYesClickListener: OnYesClickListener){
        yesClickListener = onYesClickListener
    }

    override fun getItemViewType(position: Int): Int {
        if (position == messages.size - 1 && messages.size >= 6) {
            return VIEW_TYPE_END
        }
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
        }
    }

    inner class EndViewHolder(val binding: ItemChatEndBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatData) {
            binding.tvChatEndYes.setOnClickListener {
                yesClickListener.onYesClicked()
            }
            binding.tvChatEndNo.setOnClickListener {
                // no 버튼 로직 구현
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_SEND -> SendViewHolder(ItemChatSendBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            VIEW_TYPE_RECEIVE -> ReceiveViewHolder(ItemChatReceiveBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            VIEW_TYPE_END -> EndViewHolder(ItemChatEndBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SendViewHolder -> holder.bind(messages[position])
            is ReceiveViewHolder -> holder.bind(messages[position])
            is EndViewHolder -> holder.bind(messages[position])
        }
    }

    override fun getItemCount(): Int = messages.size
}
