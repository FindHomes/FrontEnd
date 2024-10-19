package com.example.findhomes.presentation.ui.wish

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findhomes.R
import com.example.findhomes.data.model.SearchCompleteResponse
import com.example.findhomes.data.model.WishHistoryResponse
import com.example.findhomes.databinding.FragmentRecentViewBinding
import com.example.findhomes.databinding.FragmentSearchHistoryBinding
import com.example.findhomes.presentation.ui.search.SearchDetailFragment

class SearchHistoryFragment : Fragment() {
    lateinit var binding : FragmentSearchHistoryBinding
    private lateinit var historyAdapter: HistoryAdapter
    private val viewModel: WishViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchHistoryBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.loadHistoryData()

        initRecyclerView()
        observeViewModel()

        return binding.root
    }

    private fun initRecyclerView() {
        historyAdapter = HistoryAdapter()
        binding.rvSearchHistory.adapter = historyAdapter
        binding.rvSearchHistory.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL, false)

        historyAdapter.setOnItemClickListener(object : HistoryAdapter.OnItemClickListener{
            override fun onItemClicked(data: WishHistoryResponse) {
                val bundle = Bundle()
                bundle.putInt("logId", data.searchLogId)

//                val nextFragment = SearchDetailFragment()
//                nextFragment.arguments = bundle
//                parentFragmentManager.beginTransaction()
//                    .replace(R.id.main_frm, nextFragment)
//                    .addToBackStack(null)
//                    .commit()
            }

        })
    }

    private fun observeViewModel() {
        viewModel.historyData.observe(viewLifecycleOwner){ historyData ->
            historyAdapter.submitList(historyData)
        }
    }
}