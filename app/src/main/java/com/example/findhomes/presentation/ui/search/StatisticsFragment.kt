package com.example.findhomes.presentation.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findhomes.databinding.FragmentShowStatisticBinding
import com.google.android.flexbox.FlexboxLayoutManager

class StatisticsFragment : Fragment() {
    lateinit var binding : FragmentShowStatisticBinding
    private val viewModel: SearchViewModel by activityViewModels()
    private lateinit var statisticFacilityAdapter : StatisticFacilityAdapter
    private lateinit var statisticPublicAdapter: StatisticPublicAdapter
    private lateinit var statisticsKeywordAdapter: StatisticsKeywordAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowStatisticBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.loadSearchStatisticsData()

        initBack()
        initRecyclerView()
        observeViewModel()

        return binding.root
    }

    private fun initRecyclerView() {
        statisticFacilityAdapter = StatisticFacilityAdapter(requireContext())
        binding.statisticRvFacility.adapter = statisticFacilityAdapter
        binding.statisticRvFacility.layoutManager = LinearLayoutManager(requireContext())

        statisticPublicAdapter = StatisticPublicAdapter(requireContext())
        binding.statisticRvPublic.adapter = statisticPublicAdapter
        binding.statisticRvPublic.layoutManager = LinearLayoutManager(requireContext())

        // 키워드 어댑터 초기화, 선택 이벤트 처리를 위해 콜백 전달
        statisticsKeywordAdapter = StatisticsKeywordAdapter { keyword ->
            viewModel.statisticsData.value?.find { it.keyword == keyword }?.let { selectedData ->
                statisticFacilityAdapter.submitList(selectedData.facilityAndInfos)
                statisticPublicAdapter.submitList(selectedData.publicDataAndInfos)
            }
        }
        binding.statisticRvKeyword.adapter = statisticsKeywordAdapter
        binding.statisticRvKeyword.layoutManager = FlexboxLayoutManager(requireContext())
    }

    private fun observeViewModel() {
        viewModel.keywords.observe(viewLifecycleOwner) { keywords ->
            statisticsKeywordAdapter.submitList(keywords)
        }
    }

    private fun initBack() {
        binding.ivBtnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}
