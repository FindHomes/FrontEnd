package com.example.findhomes.presentation.ui.search

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.findhomes.R
import com.example.findhomes.data.model.County
import com.example.findhomes.databinding.ActivityContractRegionBinding
import com.example.findhomes.data.dataprovider.DataProvider
import com.example.findhomes.data.model.City
import com.example.findhomes.data.model.ManConRequest

class RegionSelectActivity : AppCompatActivity() {
    lateinit var binding: ActivityContractRegionBinding
    private lateinit var cityAdapter: RegionCityAdapter
    private lateinit var countyAdapter: RegionCountyAdapter
    private var selectedCity: City? = null // 현재 선택된 도시 정보를 저장

    private val cityNameMapping = mapOf(
        "서울" to "서울특별시",
        "부산" to "부산광역시",
        "경기" to "경기도"
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContractRegionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val manConRequest = intent.getSerializableExtra("manConRequest") as ManConRequest
        Log.d("manConRequest", manConRequest.toString())
        initBack()
        initRecyclerView()
        initNext(manConRequest)
    }

    private fun initBack() {
        binding.ivBtnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            overridePendingTransition(R.anim.stay_in_place, R.anim.slide_out_right)
        }
    }

    private fun initNext(manConRequest: ManConRequest) {
        binding.btnNext.setOnClickListener {
            val city = selectedCity?.name?.let { cityName ->
                cityNameMapping[cityName] ?: cityName // Map에서 찾고, 없으면 기본 이름 사용
            } ?: ""
            val district =
                if (countyAdapter.selectedPosition != RecyclerView.NO_POSITION) {
                    countyAdapter.counties[countyAdapter.selectedPosition].name
                } else
                    ""

            manConRequest.region.city = district
            manConRequest.region.district = city

            val intent = Intent(this, ChatDetailActivity::class.java)
            intent.putExtra("manConRequest", manConRequest)

            startActivity(intent)
        }
    }


    @SuppressLint("SetTextI18n")
    private fun initRecyclerView() {
        cityAdapter = RegionCityAdapter(DataProvider.cities) { city ->
            selectedCity = city // 사용자가 클릭한 도시 정보를 저장
            countyAdapter.updateCounties(city.counties)
        }

        binding.rvCity.adapter = cityAdapter
        binding.rvCity.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        selectedCity = DataProvider.cities.firstOrNull()
        countyAdapter = RegionCountyAdapter(selectedCity?.counties ?: emptyList())
        binding.rvCounty.adapter = countyAdapter
        binding.rvCounty.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        countyAdapter.setOnCountyClickListener(object : RegionCountyAdapter.OnCountyClickListener {
            @SuppressLint("SetTextI18n")
            override fun onCountyClicked(data: County) {
                binding.tvSelectRegion.text = "${selectedCity?.name} ${data.name}"
            }
        })

        // default 는 서울시 젤 위에 표시
        if (selectedCity != null && selectedCity!!.counties.isNotEmpty()) {
            val initialCounty = selectedCity!!.counties.first()
            binding.tvSelectRegion.text = "${selectedCity?.name} ${initialCounty.name}"
        }

        // 초기 선택 상태 반영
        countyAdapter.selectedPosition = 0
        countyAdapter.notifyDataSetChanged()
    }
}