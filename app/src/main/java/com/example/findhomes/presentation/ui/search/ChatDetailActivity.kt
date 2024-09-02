package com.example.findhomes.presentation.ui.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findhomes.MainActivity
import com.example.findhomes.data.model.ChatData
import com.example.findhomes.databinding.ActivitySearchDetailBinding
import com.example.findhomes.remote.AuthService
import com.example.findhomes.remote.SearchChatResponse
import com.example.findhomes.remote.SearchChatView


class ChatDetailActivity : AppCompatActivity(), SearchChatView{
    lateinit var binding: ActivitySearchDetailBinding
    private lateinit var chatAdapter : ChatDetailAdapter
    private var messages: MutableList<ChatData> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initBefore()

        binding.ivConditionConfirm.setOnClickListener {
            initChat()
        }
    }

    private fun initBefore() {
        binding.ivBtnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initChat() {
        binding.clDefaultCenter.visibility = View.GONE
        binding.rvChatMessage.visibility = View.VISIBLE

        chatAdapter = ChatDetailAdapter(messages)
        binding.rvChatMessage.adapter = chatAdapter
        binding.rvChatMessage.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val inputText = binding.etConditionInput.text.toString()
        if (inputText.isNotBlank()) {
            addMessage(ChatData(inputText, false))
            initDataManager(inputText)
            binding.etConditionInput.text.clear()
        }

        chatAdapter.setYesClickListener(object : ChatDetailAdapter.OnYesClickListener {
            override fun onYesClicked() {
                val intent = Intent(this@ChatDetailActivity, MainActivity::class.java).apply {
                    Log.d("intent",intent.toString())
                    putExtra("openFragment", "searchFragment")
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                }
                startActivity(intent)
            }

        })
    }

    private fun initDataManager(inputText : String) {
        val authService = AuthService()
        authService.setSearchChatView(this)
        authService.searchChat(inputText)
    }

    private fun addMessage(message: ChatData) {
        messages.add(message)
        chatAdapter.notifyItemInserted(messages.size - 1)
        binding.rvChatMessage.scrollToPosition(messages.size - 1)
    }

    override fun SearchChatLoading() { }

    override fun SearchChatSuccess(content: SearchChatResponse) {
        val chatMessage = content.chatResponse
        addMessage(ChatData(chatMessage, true))
    }

    override fun SearchChatFailure(code: Int, message: String) {
        TODO("Not yet implemented")
    }
}