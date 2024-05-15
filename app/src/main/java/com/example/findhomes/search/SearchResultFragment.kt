package com.example.findhomes.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findhomes.R
import com.example.findhomes.data.RankingInfo
import com.example.findhomes.databinding.FragmentSearchResultBinding

class SearchResultFragment : Fragment() {
    private lateinit var binding: FragmentSearchResultBinding
    private var rankingAdapter: ResultRankingAdapter ?= null
    private var rankingDataList: ArrayList<RankingInfo> = arrayListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchResultBinding.inflate(inflater, container, false)

        initRecyclerView()

        return binding.root
    }

    private fun initRecyclerView() {
        rankingAdapter = ResultRankingAdapter(rankingDataList)
        binding.rvResultRanking.adapter = rankingAdapter
        binding.rvResultRanking.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
    }
}