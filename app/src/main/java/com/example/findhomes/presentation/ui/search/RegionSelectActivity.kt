package com.example.findhomes.presentation.ui.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findhomes.data.model.County
import com.example.findhomes.databinding.ActivityContractRegionBinding
import com.example.findhomes.data.dataprovider.DataProvider
import com.example.findhomes.data.model.ManConRequest

class RegionSelectActivity : AppCompatActivity() {
    lateinit var binding: ActivityContractRegionBinding
    private lateinit var cityAdapter: RegionCityAdapter
    private lateinit var countyAdapter: RegionCountyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContractRegionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val manConRequest = intent.getSerializableExtra("manConRequest") as ManConRequest
        Log.d("manConRequest", manConRequest.toString())
        initBefore()
        initNext(manConRequest)
        initRecyclerView()
    }

    private fun initBefore() {
        binding.btnBefore.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initNext(manConRequest: ManConRequest) {
        binding.btnNext.setOnClickListener {
            val intent = Intent(this, ChatDetailActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initRecyclerView() {
        cityAdapter = RegionCityAdapter(DataProvider.cities) { counties ->
            countyAdapter.updateCounties(counties)
        }

        binding.rvCity.adapter = cityAdapter
        binding.rvCity.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        countyAdapter = RegionCountyAdapter(DataProvider.cities[0].counties)
        binding.rvCounty.adapter = countyAdapter
        binding.rvCounty.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        countyAdapter.setOnCountyClickListener(object : RegionCountyAdapter.OnCountyClickListener {
            override fun onCountyClicked(data: County) {
                binding.tvSelectRegion.text = data.name
            }
        })
    }
}