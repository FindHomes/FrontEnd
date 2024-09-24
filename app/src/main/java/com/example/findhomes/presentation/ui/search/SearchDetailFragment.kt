package com.example.findhomes.presentation.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findhomes.R
import com.example.findhomes.databinding.FragmentSearchDetailBinding
import com.example.findhomes.presentation.ui.search.SearchFragment

class SearchDetailFragment : Fragment() {
    lateinit var binding : FragmentSearchDetailBinding
    private val viewModel: SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchDetailBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        arguments?.let {
            val houseId = it.getInt("houseId", -1)
            if (houseId != -1) {
                viewModel.loadSearchDetailData(houseId)
            }
        }

        initBack()
        observeViewModel()

        return binding.root
    }

    private fun observeViewModel() {
        viewModel.detailData.observe(viewLifecycleOwner) {
            // 키워드 recyclerview 예정
        }
    }

    private fun initBack() {
        binding.ivBtnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}