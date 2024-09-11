package com.example.findhomes.presentation.ui.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findhomes.MainActivity
import com.example.findhomes.data.model.ManConRequest
import com.example.findhomes.databinding.ActivityChatDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatDetailActivity : AppCompatActivity(){
    lateinit var binding: ActivityChatDetailBinding
    private lateinit var recommendAdapter: ChatRecommendAdapter
    private lateinit var chatAdapter : ChatDetailAdapter
    private var messages: MutableList<ChatData> = mutableListOf()
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatDetailBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setContentView(binding.root)

        val manConRequest = intent.getSerializableExtra("manConRequest") as ManConRequest
        Log.d("manConRequest", manConRequest.toString())

        viewModel.postManConData(manConRequest)

        observeViewModel()
        initRecyclerView()


        initBefore()
        showLoadingAnimation(true) // 애니메이션 시작

        binding.ivConditionConfirm.setOnClickListener {
            initChat()
        }
    }

    private fun showLoadingAnimation(show: Boolean) {
        binding.chatLaLoading.visibility = if (show) View.VISIBLE else View.GONE
        binding.clChat.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun initRecyclerView() {
        recommendAdapter = ChatRecommendAdapter()
        binding.rvRecommend.adapter = recommendAdapter
        binding.rvRecommend.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun observeViewModel() {
        viewModel.recommendData.observe(this) { recommendData ->
            recommendAdapter.submitList(recommendData) {
                showLoadingAnimation(false)
            }
        }
        viewModel.chatData.observe(this) { chatData ->
            chatData?.let {
                addMessage(it)
            }
        }

    }

    private fun initBefore() {
        binding.ivBtnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initChat() {
        binding.clDefaultCenter.visibility = View.GONE
        binding.rvRecommend.visibility = View.GONE
        binding.rvChatMessage.visibility = View.VISIBLE

        chatAdapter = ChatDetailAdapter(messages)
        binding.rvChatMessage.adapter = chatAdapter
        binding.rvChatMessage.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val inputText = binding.etConditionInput.text.toString()
        Log.d("inputText", inputText)
        if (inputText.isNotBlank()) {
            // 입력받은 텍스트를 viewModel로 보내기
            viewModel.sendUserMessage(inputText)
            // 입력 완료되면 지우기
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

    private fun addMessage(message: ChatData) {
        messages.add(message)
        chatAdapter.notifyItemInserted(messages.size - 1)
        binding.rvChatMessage.scrollToPosition(messages.size - 1)
    }

}