package com.homes.findhomes.presentation.ui.chat

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.homes.findhomes.MainActivity
import com.homes.findhomes.R
import com.homes.findhomes.data.model.ManConRequest
import com.homes.findhomes.databinding.ActivityChatDetailBinding
import com.homes.findhomes.presentation.ui.search.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatDetailActivity : AppCompatActivity(){
    lateinit var binding: ActivityChatDetailBinding
    private lateinit var recommendAdapter: ChatRecommendAdapter
    private lateinit var chatAdapter : ChatDetailAdapter
    private var messages: MutableList<ChatData> = mutableListOf()
    private lateinit var manConRequest : ManConRequest
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatDetailBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setContentView(binding.root)

        manConRequest = intent.getSerializableExtra("manConRequest") as ManConRequest
        Log.d("manConRequest", manConRequest.toString())

        viewModel.postManConData(manConRequest)

        observeViewModel()
        initRecommend()


        initBack()
        showLoadingAnimation(true) // 애니메이션 시작

        binding.ivConditionConfirm.setOnClickListener {
            initChat()
        }
    }

    private fun showLoadingAnimation(show: Boolean) {
        binding.chatLaLoading.visibility = if (show) View.VISIBLE else View.GONE
        binding.clChat.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun initRecommend() {
        recommendAdapter = ChatRecommendAdapter()
        binding.rvRecommend.adapter = recommendAdapter
        binding.rvRecommend.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        recommendAdapter.setOnItemClickListener(object : ChatRecommendAdapter.OnItemClickListener{
            override fun onItemClicked(data: String) {
                initChat(data)
            }

        })
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

    private fun initBack() {
        binding.ivBtnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            overridePendingTransition(R.anim.stay_in_place, R.anim.slide_out_right)
        }
    }

    private fun initChat(inputText: String? = null) {
        binding.clDefaultCenter.visibility = View.GONE
        binding.rvRecommend.visibility = View.GONE
        binding.rvChatMessage.visibility = View.VISIBLE
        binding.tvChatForceEnd.visibility = View.VISIBLE

        chatAdapter = ChatDetailAdapter(messages)
        binding.rvChatMessage.adapter = chatAdapter
        binding.rvChatMessage.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val sendText = inputText ?: binding.etConditionInput.text.toString()
        if (sendText.isNotBlank()) {
            viewModel.sendUserMessage(sendText)
            binding.etConditionInput.text.clear()
        }

        // 강제로 넘어가기 버튼
        binding.tvChatForceEnd.setOnClickListener {
            val intent = Intent(this@ChatDetailActivity, MainActivity::class.java).apply {
                Log.d("intent",intent.toString())
                putExtra("openFragment", "searchFragment")
                putExtra("manConRequest", manConRequest)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            val options = ActivityOptions.makeCustomAnimation(this@ChatDetailActivity, R.anim.slide_in_right, R.anim.stay_in_place)
            startActivity(intent, options.toBundle())
        }
    }

    private fun addMessage(message: ChatData) {
        messages.add(message)
        chatAdapter.notifyItemInserted(messages.size - 1)
        binding.rvChatMessage.scrollToPosition(messages.size - 1)
    }

}