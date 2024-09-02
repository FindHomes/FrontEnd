package com.example.findhomes.presentation.ui.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.findhomes.data.model.RankingInfo
import com.example.findhomes.data.model.SearchResultData
import com.example.findhomes.databinding.FragmentSearchResultBinding

class SearchResultFragment : Fragment() {
    private lateinit var binding: FragmentSearchResultBinding
    private var rankingAdapter: ResultRankingAdapter?= null
    private var rankingDataList: ArrayList<RankingInfo> = arrayListOf()
    var dummyData : ArrayList<SearchResultData> = arrayListOf()
    private lateinit var categories: ArrayList<String>
    private lateinit var contracts: ArrayList<String>
    private lateinit var regions: ArrayList<String>
    lateinit var details: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchResultBinding.inflate(inflater, container, false)

        val bundle = arguments
        if (bundle != null) {
            val category = bundle.getStringArrayList("category")
            val contract = bundle.getStringArrayList("contract")
            val region = bundle.getStringArrayList("region")
            val detail = bundle.getString("detail")

            categories = category!!
            contracts = contract!!
            regions = region!!
            details = detail!!

            Log.d("category 출력", categories.toString())
            Log.d("contract 출력", contracts.toString())
            Log.d("region 출력", regions.toString())
            Log.d("detail", details.toString())

            initSearchOutputManager(categories, contracts, regions, details)
        }

        return binding.root
    }

    private fun initSearchOutputManager(
        categories: ArrayList<String>,
        contracts: ArrayList<String>,
        regions: ArrayList<String>,
        details: String
    ) {
//        val token = getJwt()
//        Log.d("token",token)
//        val prices = PricesData(400000000, 0, WSData(0,0))
//        val manCon = EssentialData(categories, prices, regions)
//        val authService = AuthService()
//        authService.setSearchResultView(this)
//        authService.searchResultInfo(manCon, details)

    }

}