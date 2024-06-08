package com.example.findhomes.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findhomes.ApplicationClass
import com.example.findhomes.data.RankingInfo
import com.example.findhomes.data.SearchResultData
import com.example.findhomes.databinding.FragmentSearchResultBinding
import com.example.findhomes.remote.AuthService
import com.example.findhomes.remote.EssentialData
import com.example.findhomes.remote.PricesData
import com.example.findhomes.remote.SearchResultResponse
import com.example.findhomes.remote.SearchResultView
import com.example.findhomes.remote.WSData

class SearchResultFragment : Fragment(), SearchResultView {
    private lateinit var binding: FragmentSearchResultBinding
    private var rankingAdapter: ResultRankingAdapter ?= null
    private var rankingDataList: ArrayList<RankingInfo> = arrayListOf()
    private var rankingResultData : ArrayList<SearchResultResponse> = arrayListOf()
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

//            initSearchOutputManager(categories, contracts, regions, details)
        }

        initData()
        initRecyclerView()

        return binding.root
    }

    private fun initData() {
        dummyData.addAll(
            arrayListOf(
                // 잠원 월드메르디앙, 잠원대우아이빌, 잠원대우아이빌, 대주피오레, 마일스디오빌
                SearchResultData("https://lh3.googleusercontent.com/p/AF1QipP4dXYmInFnlpPhZzcdl9dg1I0hRt9nd8Py3v1C=s680-w680-h5101", "매매", "13억 5000만원", "12층 107.46m 관리비 26만", "주차대수 가구당 1.23"),
                SearchResultData(""2,"매매", "4억 7000만원", "2층 39.6m 관리비 8만", "주차대수 가구당 1.0"),
                SearchResultData(3,"월세", "2억 8000/25", "10층 51m 관리비 8.7만", "주차대수 가구당 1.0"),
                SearchResultData("https://lh3.googleusercontent.com/p/AF1QipORF8qz6KOFYsa09CiZ3_9QbgqTztCf4zE-MJyA=s680-w680-h510"4,"매매", "16억 9000만원", "고층 139.1m 관리비 22만", "주차가능"),
                SearchResultData(5,"월세", "110/180", "6층 47.19m 관리비 15만", "주차가능")
                )
        )
    }

    private fun initSearchOutputManager(
        categories: ArrayList<String>,
        contracts: ArrayList<String>,
        regions: ArrayList<String>,
        details: String
    ) {
//        val token = getJwt()
//        Log.d("token",token)
        val prices = PricesData(400000000, 0, WSData(0,0))
        val manCon = EssentialData(categories, prices, regions)
        val authService = AuthService()
        authService.setSearchResultView(this)
        authService.searchResultInfo(manCon, details)

    }


    private fun initRecyclerView() {
        rankingAdapter = ResultRankingAdapter(dummyData)
        binding.rvResultRanking.adapter = rankingAdapter
        binding.rvResultRanking.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
    }

    override fun SearchResultLoading() {
        TODO("Not yet implemented")
    }

    override fun SearchResultSuccess(content: ArrayList<SearchResultResponse>) {
        rankingResultData.clear()
        rankingResultData.addAll(content)
        Log.d("content", content.toString())
        rankingAdapter?.notifyDataSetChanged()
    }

    override fun SearchResultFailure(status: Int, message: String) {
        Log.d("통신 에러", message.toString())
    }
}