package com.example.findhomes.presentation.ui.search

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.findhomes.R
import com.example.findhomes.data.model.SearchResultData
import com.example.findhomes.databinding.FragmentSearchBinding
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.material.bottomsheet.BottomSheetBehavior

class SearchFragment : Fragment(), OnMapReadyCallback {
    lateinit var binding: FragmentSearchBinding
    private lateinit var naverMap: NaverMap
    private lateinit var rankingAdapter: ResultRankingAdapter
    private var resultDataList: ArrayList<SearchResultData> = arrayListOf()
    private var selectedMarker: Marker? = null
    private var markerMap = mutableMapOf<Int, Marker>()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)

        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_view) as MapFragment? ?: MapFragment.newInstance().also {
            fm.beginTransaction().add(R.id.map_view, it).commit()
        }
        mapFragment.getMapAsync(this)
        initRecyclerView()
        initStatisticFragment()
        initBottomSheetBehavior()
        return binding.root
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        initMarker()
        initCameraPosition()
        if (resultDataList.isNotEmpty()) {
            updateMap(0) // 첫 번째 마커 선택
        }
    }

    private fun initMarker() {
        resultDataList.forEachIndexed { index, resultData ->
            val marker = Marker()
            marker.position = LatLng(resultData.lat, resultData.lon)
            marker.map = naverMap
            marker.icon = MarkerIcons.BLACK
            marker.iconTintColor = if (index == 0) Color.RED else Color.BLUE
            markerMap[index] = marker
        }
    }

    private fun updateMap(position: Int) {
        resultDataList.getOrNull(position)?.let {
            val location = LatLng(it.lat, it.lon)
            naverMap.moveCamera(CameraUpdate.scrollTo(location))
            selectedMarker?.map = null  // 이전 마커 선택 해제
            selectedMarker = markerMap[position]
            selectedMarker?.map = naverMap  // 새로운 마커 선택
        }
    }

    private fun initCameraPosition() {
        // Camera position 초기화
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val position = markerMap.entries.find { it.value == marker }?.key ?: return false
        updateSelect(position)
//        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED // 마커 클릭 시 BottomSheet 표시
        return true
    }

    private fun updateSelect(position: Int) {
        // 선택된 마커 처리 로직
    }
}