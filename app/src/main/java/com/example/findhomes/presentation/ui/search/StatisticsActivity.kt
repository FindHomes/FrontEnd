package com.example.findhomes.presentation.ui.search

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findhomes.databinding.ActivityShowStatisticsBinding
import com.google.android.flexbox.FlexboxLayoutManager

class StatisticsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowStatisticsBinding
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var statisticFacilityAdapter: StatisticFacilityAdapter
    private lateinit var statisticPublicAdapter: StatisticPublicAdapter
    private lateinit var statisticsKeywordAdapter: StatisticsKeywordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowStatisticsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.loadSearchStatisticsData()

        initBackButton()
        initRecyclerViews()
        observeViewModel()
    }

    private fun initRecyclerViews() {
        statisticFacilityAdapter = StatisticFacilityAdapter(this)
        binding.statisticRvFacility.adapter = statisticFacilityAdapter
        binding.statisticRvFacility.layoutManager = LinearLayoutManager(this)

        statisticPublicAdapter = StatisticPublicAdapter(this)
        binding.statisticRvPublic.adapter = statisticPublicAdapter
        binding.statisticRvPublic.layoutManager = LinearLayoutManager(this)

        statisticsKeywordAdapter = StatisticsKeywordAdapter { keyword ->
            viewModel.statisticsData.value?.find { it.keyword == keyword }?.let { selectedData ->
                statisticFacilityAdapter.submitList(selectedData.facilityAndInfos)
                statisticPublicAdapter.submitList(selectedData.publicDataAndInfos)
            }
        }
        binding.statisticRvKeyword.adapter = statisticsKeywordAdapter
        binding.statisticRvKeyword.layoutManager = FlexboxLayoutManager(this)
    }

    private fun observeViewModel() {
        viewModel.keywords.observe(this) { keywords ->
            statisticsKeywordAdapter.submitList(keywords)
        }
    }

    private fun initBackButton() {
        binding.ivBtnBack.setOnClickListener {
            onBackPressed()
        }
    }
}
