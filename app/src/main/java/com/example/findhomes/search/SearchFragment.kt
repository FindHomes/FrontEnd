package com.example.findhomes.search

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.findhomes.R
import com.example.findhomes.data.SearchResultData
import com.example.findhomes.databinding.FragmentSearchBinding
import com.example.findhomes.databinding.ItemMarkerViewBinding
import com.example.findhomes.remote.AuthService
import com.example.findhomes.remote.SearchCompleteResponse
import com.example.findhomes.remote.SearchCompleteView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlin.math.abs

class SearchFragment : Fragment(), OnMapReadyCallback, OnMarkerClickListener, OnMapClickListener, SearchCompleteView{
    lateinit var binding: FragmentSearchBinding
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var rankingAdapter: ResultRankingAdapter
    private var resultDataList : ArrayList<SearchResultData> = arrayListOf()
    private var selectedMarker: Marker? = null
    private var markerMap = mutableMapOf<Int, Marker>()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private var xmin: Double? = null
    private var xmax: Double? = null
    private var ymin: Double? = null
    private var ymax: Double? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        initDataManager()

        initMap(savedInstanceState)
        initRecyclerView()
        initStatisticFragment()
        initBottomSheetBehavior()

        return binding.root
    }

    private fun initBottomSheetBehavior() {
        // BottomSheetBehavior 초기화
        bottomSheetBehavior = BottomSheetBehavior.from(binding.rvResultRanking)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN // 초기 상태 설정
    }

    private fun initMap(savedInstanceState: Bundle?) {
        mapView = binding.mvRanking
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        googleMap.setOnMarkerClickListener(this)
        googleMap.setOnMapClickListener(this)
        initMarker()
        initCameraPosition()
        if (resultDataList.isNotEmpty()) {
            updateMap(0) // 첫 번째 마커 선택
        }
    }

    private fun initMarker() {
        resultDataList.forEachIndexed { index, resultData ->
            addCustomMarker(resultData, index)
        }
    }

    private fun addCustomMarker(resultData: SearchResultData, index: Int) {
        val binding: ItemMarkerViewBinding = ItemMarkerViewBinding.inflate(layoutInflater)
        binding.tvRanking.text = (index + 1).toString()
        binding.tvPrice.text = "${resultData.price}만원"

        val markerView = binding.root
        val markerBitmap = createBitmapFromView(markerView)

        val markerOptions = MarkerOptions()
            .position(LatLng(resultData.lon, resultData.lat))
            .icon(BitmapDescriptorFactory.fromBitmap(markerBitmap))
            .zIndex(1000f - index) // 초기 zIndex 설정 (랭킹 기반)

        val marker = googleMap.addMarker(markerOptions)
        marker?.tag = resultData
        markerMap[index] = marker!!

        rankingAdapter.submitList(resultDataList.toList())
    }


    private fun updateMap(position: Int) {
        resultDataList.getOrNull(position)?.let {
            val location = LatLng(it.lon, it.lat)
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(location)) // 줌 레벨 설정 가능
            selectedMarker?.let { marker ->
                updateMarkerView(marker, false)  // 이전 마커 선택 해제
            }
            selectedMarker = markerMap[position]
            selectedMarker?.let { marker ->
                updateMarkerView(marker, true)  // 새로운 마커 선택
            }
        }
    }


    private fun initStatisticFragment() {
        binding.btnStatisticShow.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("key", "value")

            val nextFragment = SearchDetailFragment()
            nextFragment.arguments = bundle
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frm, nextFragment)
                .commit()
        }
    }

    private fun initRecyclerView() {
        rankingAdapter = ResultRankingAdapter(requireContext())

        binding.rvResultRanking.adapter = rankingAdapter
        binding.rvResultRanking.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvResultRanking)

        binding.rvResultRanking.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val position = layoutManager.findFirstCompletelyVisibleItemPosition()
                    if (position != RecyclerView.NO_POSITION) {
                        updateSelect(position)
                    }
                }
            }
        })

        rankingAdapter.setOnItemClickListener(object : ResultRankingAdapter.OnItemClickListener{
            override fun onItemClicked(data: SearchResultData) {
                val bundle = Bundle()
                bundle.putString("key", "value")

                val nextFragment = SearchDetailFragment()
                nextFragment.arguments = bundle
                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, nextFragment)
                    .commit()
            }
        })
    }


    private fun initCameraPosition() {
        if (xmin != null && xmax != null && ymin != null && ymax != null) {
            val bounds = LatLngBounds(LatLng(ymin!!, xmin!!), LatLng(ymax!!, xmax!!))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100)) // 100은 내부 패딩입니다.
        }
    }

    private fun createBitmapFromView(view: View): Bitmap {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }


    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val position = markerMap.entries.find { it.value == marker }?.key ?: return false
        updateSelect(position)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED // 마커 클릭 시 BottomSheet 표시
        return true
    }


    override fun onMapClick(latLng: LatLng) {
        selectedMarker?.let {
            updateMarkerView(it, false)
        }
        selectedMarker = null
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN // 맵 클릭 시 BottomSheet 숨김
    }

    private fun updateSelect(position: Int) {
        // 데이터 유효성 검사
        resultDataList.getOrNull(position)?.let {
            val location = LatLng(it.lon, it.lat)
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(location))

            // 이전 선택된 마커의 상태 업데이트
            selectedMarker?.let { marker ->
                updateMarkerView(marker, false)
                marker.zIndex = (1000f - markerMap.entries.find { entry -> entry.value == marker }?.key!!) // 기본 zIndex 복구
            }

            // 새로운 마커 선택 및 zIndex 설정
            selectedMarker = markerMap[position]
            selectedMarker?.let { marker ->
                updateMarkerView(marker, true)
                marker.zIndex = 10000f // 선택된 마커가 항상 위로 올라오도록 높은 zIndex 설정
            }
        }

        // RecyclerView 스크롤
        binding.rvResultRanking.scrollToPosition(position)
    }

    private fun updateMarkerView(marker: Marker?, isSelected: Boolean) {
        marker?.let {
            val markerItem = it.tag as SearchResultData
            val binding: ItemMarkerViewBinding = ItemMarkerViewBinding.inflate(layoutInflater)
            val index = markerMap.entries.find { entry -> entry.value == marker }?.key ?: -1

            binding.root.isSelected = isSelected
            binding.tvRanking.text = (index+1).toString()
            binding.tvPrice.text = "${markerItem.price}만원"

            // 아이콘 업데이트
            val newIcon = BitmapDescriptorFactory.fromBitmap(createBitmapFromView(binding.root))
            it.setIcon(newIcon)
        }
    }


    private fun initDataManager() {
        val authService = AuthService()
        authService.setSearchCompleteView(this)
        authService.searchComplete()
    }

    override fun SearchCompleteLoading() {
        TODO("Not yet implemented")
    }

    override fun SearchCompleteSuccess(content: SearchCompleteResponse) {
        xmin = content.xmin
        xmax = content.xmax
        ymin = content.ymin
        ymax = content.ymax

        resultDataList.clear()
        resultDataList.addAll(content.houses.map { house ->
            SearchResultData(
                houseId = house.houseId,
                price = house.price.toString(),
                priceType = house.priceType,
                room = house.roomNum.toString(),
                washRoom = house.washroomNum.toString(),
                img = house.imgUrl,
                lat = house.x,
                lon = house.y,
                score = house.score.toInt(),
                size = house.size
            )
        })

    }

    override fun SearchCompleteFailure(code: Int, message: String) {
        TODO("Not yet implemented")
    }

}
