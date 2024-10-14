package com.example.findhomes.presentation.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.findhomes.databinding.FragmentShowStatisticBinding

class SearchStatisticFragment : Fragment() {
    lateinit var binding : FragmentShowStatisticBinding
    private val viewModel: SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowStatisticBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.loadSearchStatisticsData()

        initBack()

        return binding.root
    }

    private fun observeViewModel() {
        viewModel.statisticsData.observe(viewLifecycleOwner) {
            // 키워드 recyclerview 예정
        }
    }


    private fun initBack() {
        binding.ivBtnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}