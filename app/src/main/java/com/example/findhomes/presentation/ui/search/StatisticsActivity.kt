package com.example.findhomes.presentation.ui.search

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findhomes.databinding.ActivityShowStatisticsBinding
import com.google.android.flexbox.FlexboxLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
                if (selectedData.facilityAndInfos.isEmpty() && selectedData.publicDataAndInfos.isEmpty()) {
                    // 데이터가 없을 경우
                    binding.statisticClNoDta.visibility = View.VISIBLE
                    binding.statisticRvFacility.visibility = View.GONE
                    binding.statisticRvPublic.visibility = View.GONE
                } else {
                    // 데이터가 있을 경우
                    binding.statisticClNoDta.visibility = View.GONE
                    binding.statisticRvFacility.visibility = View.VISIBLE
                    binding.statisticRvPublic.visibility = View.VISIBLE
                    statisticFacilityAdapter.submitList(selectedData.facilityAndInfos)
                    statisticPublicAdapter.submitList(selectedData.publicDataAndInfos)
                }
            }
        }
        binding.statisticRvKeyword.adapter = statisticsKeywordAdapter
        binding.statisticRvKeyword.layoutManager = FlexboxLayoutManager(this)
    }

    private fun observeViewModel() {
        viewModel.keywords.observe(this) { keywords ->
            statisticsKeywordAdapter.submitList(keywords)
            if (keywords.isNotEmpty()) {
                statisticsKeywordAdapter.selectedPosition = 0 // 첫 번째 아이템 선택
                statisticsKeywordAdapter.notifyItemChanged(0)
                selectKeyword(keywords.first()) // 첫 번째 키워드 선택
            }
        }
    }

    private fun selectKeyword(keyword: String) {
        viewModel.statisticsData.value?.find { it.keyword == keyword }?.let { selectedData ->
            if (selectedData.facilityAndInfos.isEmpty() && selectedData.publicDataAndInfos.isEmpty()) {
                // 데이터가 없을 경우
                binding.statisticClNoDta.visibility = View.VISIBLE
                binding.statisticRvFacility.visibility = View.GONE
                binding.statisticRvPublic.visibility = View.GONE
            } else {
                // 데이터가 있을 경우
                binding.statisticClNoDta.visibility = View.GONE
                binding.statisticRvFacility.visibility = View.VISIBLE
                binding.statisticRvPublic.visibility = View.VISIBLE
                statisticFacilityAdapter.submitList(selectedData.facilityAndInfos)
                statisticPublicAdapter.submitList(selectedData.publicDataAndInfos)
            }
        }
    }

    private fun initBackButton() {
        binding.ivBtnBack.setOnClickListener {
            onBackPressed()
        }
    }
}
