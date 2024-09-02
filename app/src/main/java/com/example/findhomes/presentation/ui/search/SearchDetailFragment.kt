package com.example.findhomes.presentation.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findhomes.R
import com.example.findhomes.data.model.DetailPropertyData
import com.example.findhomes.data.model.SearchDetailData
import com.example.findhomes.databinding.FragmentSearchDetailBinding

class SearchDetailFragment : Fragment() {
    lateinit var binding : FragmentSearchDetailBinding
    lateinit var detailPropertyAdapter: SearchDetailPropertyAdapter
    val propertyData : ArrayList<DetailPropertyData> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchDetailBinding.inflate(layoutInflater)

        initBack()
        initData()
        initRecyclerView()

        return binding.root
    }

    private fun initBack() {
        binding.ivBtnBack.setOnClickListener {
            val nextFragment = SearchFragment()
            val bundle = Bundle()
            bundle.putString("key", "value")

            nextFragment.arguments = bundle
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frm, nextFragment)
                .commit()
            onDestroy()
        }
    }

    private fun initRecyclerView() {
        detailPropertyAdapter = SearchDetailPropertyAdapter(propertyData)
        binding.rvDetail.adapter = detailPropertyAdapter
        binding.rvDetail.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun initData() {
        val data = SearchDetailData(
            "https://cdn.pixabay.com/photo/2017/08/21/14/21/house-2665557_1280.jpg",
            "월세 3000 / 200",
            "쓰리룸 이상, 65.1m2",
            "월세",
            "매월 12만원",
            "원룸(분리형)",
            "10층/17층",
            "42.9m/50m",
            "1개/1개",
            "개별난방",
            "-",
            "2024.07.10",
            "단독주택"
        )

        binding.tvDetailMain.text = data.mainDetail
        binding.tvDetailSub.text = data.subDetail

        binding.tvDetailRoomAnswer.text = data.infoRoom
        binding.tvDetailFloorAnswer.text = data.infoFloor
        binding.tvDetailAreaAnswer.text = data.infoArea
        binding.tvDetailRoomNumberAnswer.text = data.infoRoomNumber
        binding.tvDetailHeatAnswer.text = data.infoHeat
        binding.tvDetailCarAnswer.text = data.infoHeat
        binding.tvDetailInAnswer.text = data.infoIn
        binding.tvDetailForAnswer.text = data.infoFor

        propertyData.addAll(
            arrayListOf(
                DetailPropertyData("안전등급 상위 12%"),
                DetailPropertyData("인구밀도 상위 12%"),
                DetailPropertyData("안전등급 상위 12%"),
                DetailPropertyData("인구밀도 상위 12%")
            )
        )
    }
}