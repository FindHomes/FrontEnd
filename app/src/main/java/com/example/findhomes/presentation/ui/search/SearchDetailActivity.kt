package com.example.findhomes.presentation.ui.search

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.findhomes.R
import com.example.findhomes.databinding.ActivitySearchDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchDetailActivity : AppCompatActivity() {
    lateinit var binding : ActivitySearchDetailBinding
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchDetailBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        intent.let {
            val houseId = it.getIntExtra("houseId", -1)
            if (houseId != -1) {
                viewModel.loadSearchDetailData(houseId)
            }
        }

        initBack()
        observeViewModel()

        return setContentView(binding.root)
    }

    private fun observeViewModel() {
        viewModel.detailData.observe(this) {
            // 키워드 recyclerview 예정
        }
    }

    private fun initBack() {
        binding.ivBtnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            overridePendingTransition(R.anim.stay_in_place, R.anim.slide_out_right)
        }
    }
}