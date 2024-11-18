package com.homes.findhomes.presentation.ui.search

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.homes.findhomes.R
import com.homes.findhomes.databinding.ActivitySearchDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchDetailActivity : AppCompatActivity() {
    lateinit var binding : ActivitySearchDetailBinding
    private lateinit var propertyAdapter: SearchDetailPropertyAdapter
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchDetailBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        intent.let {
            val houseId = it.getIntExtra("houseId", -1)
            val source = it.getStringExtra("source")
            Log.d("houseId", houseId.toString())
            Log.d("source", source.toString())
            if (houseId != -1) {
                when (source) {
                    "SearchFragment" -> viewModel.loadSearchDetailData(houseId)
                    "FavoriteFragment" -> viewModel.loadSearchFavoriteDetailData(houseId)
                    else -> {}
                }
            }
        }

        initBack()
        initRecyclerView()
        observeViewModel()

        return setContentView(binding.root)
    }

    private fun initRecyclerView() {
        propertyAdapter = SearchDetailPropertyAdapter()
        binding.rvDetail.adapter = propertyAdapter
        binding.rvDetail.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun observeViewModel() {
        viewModel.statsData.observe(this) { statsData ->
            propertyAdapter.submitList(statsData)
        }
    }

    private fun initBack() {
        binding.ivBtnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            overridePendingTransition(R.anim.stay_in_place, R.anim.slide_out_right)
        }
    }
}