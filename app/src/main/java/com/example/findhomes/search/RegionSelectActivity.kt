package com.example.findhomes.search

import android.os.Bundle
import android.telephony.SignalStrength
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findhomes.data.City
import com.example.findhomes.data.County
import com.example.findhomes.databinding.ActivityContractRegionBinding
import com.example.findhomes.dataprovider.DataProvider

class RegionSelectActivity : AppCompatActivity() {
    lateinit var binding: ActivityContractRegionBinding
    private lateinit var cityAdapter: RegionCityAdapter
    private lateinit var countyAdapter: RegionCountyAdapter
    private var cities : List<City> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContractRegionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
    }
    private fun initRecyclerView() {
        cityAdapter = RegionCityAdapter(DataProvider.cities) { counties ->
            countyAdapter.updateCounties(counties)
        }

        binding.rvCity.adapter = cityAdapter
        binding.rvCity.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        countyAdapter = RegionCountyAdapter(emptyList())
        binding.rvCounty.adapter = countyAdapter
        binding.rvCounty.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }
}