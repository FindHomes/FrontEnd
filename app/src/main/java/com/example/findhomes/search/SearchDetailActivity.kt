package com.example.findhomes.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findhomes.MainActivity
import com.example.findhomes.data.ChatData
import com.example.findhomes.databinding.ActivitySearchDetailBinding


class SearchDetailActivity : AppCompatActivity(){
    lateinit var binding: ActivitySearchDetailBinding
    private lateinit var chatAdapter : SearchDetailAdapter
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

        chatAdapter = SearchDetailAdapter(messages)
        binding.rvChatMessage.adapter = chatAdapter
        binding.rvChatMessage.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val inputText = binding.etConditionInput.text.toString()
        if (inputText.isNotBlank()) {
            addMessage(ChatData(inputText, false))
            addMessage(ChatData("답변: \"$inputText\" 라고 침?", true))
            binding.etConditionInput.text.clear()
        }

        chatAdapter.setYesClickListener(object : SearchDetailAdapter.OnYesClickListener{
            override fun onYesClicked() {
                val intent = Intent(this@SearchDetailActivity, MainActivity::class.java).apply {
                    Log.d("intent",intent.toString())
                    putExtra("openFragment", "searchFragment")
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                }
                startActivity(intent)
            }

        })
    }

    private fun addMessage(message: ChatData) {
        messages.add(message)
        chatAdapter.notifyItemInserted(messages.size - 1)
        binding.rvChatMessage.scrollToPosition(messages.size - 1)
    }
}