package com.example.findhomes.presentation.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findhomes.databinding.FragmentShowStatisticBinding

class StatisticsFragment : Fragment() {
    lateinit var binding : FragmentShowStatisticBinding
    private val viewModel: SearchViewModel by activityViewModels()
    private lateinit var statisticsDataAdapter : StatisticsDataAdapter
    private lateinit var statisticsKeywordAdapter: StatisticsKeywordAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowStatisticBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.loadSearchStatisticsData()

        initBack()
        initRecyclerView()
        observeViewModel()

        return binding.root
    }

    private fun initRecyclerView() {
        statisticsDataAdapter = StatisticsDataAdapter()
        binding.statisticRvInfo.adapter = statisticsDataAdapter
        binding.statisticRvInfo.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        statisticsKeywordAdapter = StatisticsKeywordAdapter()
        binding.statisticRvKeyword.adapter = statisticsDataAdapter
        binding.statisticRvKeyword.layoutManager = FlexboxLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun observeViewModel() {
        viewModel.facilityData.observe(viewLifecycleOwner) { facilityData ->
            // 키워드 recyclerview 예정
            statisticsDataAdapter.submitList(facilityData)
        }
    }


    private fun initBack() {
        binding.ivBtnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}