package com.example.findhomes.search

import android.annotation.SuppressLint
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
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findhomes.R
import com.example.findhomes.data.SearchResultData
import com.example.findhomes.databinding.FragmentSearchBinding
import com.example.findhomes.databinding.ItemMarkerViewBinding
import com.example.findhomes.local.getJwt
import com.example.findhomes.remote.AuthService
import com.example.findhomes.remote.SearchCompleteResponse
import com.example.findhomes.remote.SearchCompleteView
import com.example.findhomes.remote.SearchUpdateResponse
import com.example.findhomes.remote.SearchUpdateView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlin.math.abs

class SearchFragment : Fragment(), OnMapReadyCallback, OnMarkerClickListener, OnMapClickListener, SearchCompleteView{
    lateinit var binding: FragmentSearchBinding
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var rankingAdapter: ResultRankingAdapter
    private var resultDataList : ArrayList<SearchResultData> = arrayListOf()
    private var selectedMarker: Marker? = null

    private var initialTouchY: Float = 0f
    private var initialPeekHeight: Int = 0
    private val minHeight by lazy { resources.displayMetrics.heightPixels / 4 }
    private val midHeight by lazy { resources.displayMetrics.heightPixels / 2 }
    private val maxHeight by lazy { resources.displayMetrics.heightPixels }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)

        initMap(savedInstanceState)
        setupBottomSheet()
        initData()
        initDataManager()
        initRankingRecyclerView()
        initStatisticFragment()

        return binding.root
    }

    private fun initMap(savedInstanceState: Bundle?) {
        mapView = binding.mvRanking
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        googleMap.setOnMarkerClickListener(this)
        getSampleMarker()
        initCameraPosition()

        // 카메라 이동 완료 리스너 등록
        googleMap.setOnCameraIdleListener {
            updateVisibleMarkers()
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

    private fun initRankingRecyclerView() {
        rankingAdapter = ResultRankingAdapter(requireContext())

        binding.rvResultRanking.adapter = rankingAdapter
        binding.rvResultRanking.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

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

    private fun initData() {
        resultDataList.addAll(
            arrayListOf(
                SearchResultData(37.5393, 127.0709,"https://cdn.pixabay.com/photo/2021/08/08/14/16/road-6531031_1280.jpg",
                    1, 100, "월세", "1,000만원", "100점 짜리", "주차공간 존재"),
                SearchResultData(37.5432, 127.071,"https://cdn.pixabay.com/photo/2019/11/20/14/48/mirror-house-4640243_1280.jpg",
                    2, 200, "월세", "1,000만원", "200점 짜리", "주차공간 존재"),
                SearchResultData(37.5413, 127.073,"https://cdn.pixabay.com/photo/2021/08/08/14/16/road-6531031_1280.jpg",
                    3, 150, "월세", "1,000만원", "150점 짜리", "주차공간 존재"),
                SearchResultData(37.5373, 127.074,"https://cdn.pixabay.com/photo/2019/11/20/14/48/mirror-house-4640243_1280.jpg",
                    4, 50, "월세", "1,000만원", "50점 짜리", "주차공간 존재"),
                SearchResultData(37.5582, 127.0729,"https://cdn.pixabay.com/photo/2021/08/08/14/16/road-6531031_1280.jpg",
                    5, 30, "월세", "1,000만원", "30점 짜리", "주차공간 존재"),
                SearchResultData(37.5632, 127.0739,"https://cdn.pixabay.com/photo/2019/11/20/14/48/mirror-house-4640243_1280.jpg",
                    6, 20, "월세", "1,000만원", "20점 짜리", "주차공간 존재"),
                SearchResultData(37.5682, 127.0749,"https://cdn.pixabay.com/photo/2021/08/08/14/16/road-6531031_1280.jpg",
                    7,700,  "월세", "1,000만원", "700점 짜리", "주차공간 존재"),
                SearchResultData(38.5732, 127.0759,"https://cdn.pixabay.com/photo/2019/11/20/14/48/mirror-house-4640243_1280.jpg",
                    8, 280,"월세", "1,000만원", "280점 짜리", "주차공간 존재"),
            )
        )
    }

    private fun setupBottomSheet() {
        val bottomSheet = binding.clBottomBar
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.isDraggable = false

        // 사용자 정의 단계 설정
        val customHeights = arrayOf(minHeight, midHeight, maxHeight)

        val viewHandler = binding.clBottomHandler
        viewHandler.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialTouchY = event.rawY
                    initialPeekHeight = bottomSheetBehavior.peekHeight
                    Log.d("taejung", "Motion down")
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val deltaY = event.rawY - initialTouchY
                    var newPeekHeight = (initialPeekHeight - deltaY).toInt().coerceIn(minHeight, maxHeight)

                    // 가장 가까운 사용자 정의 높이 찾기
                    newPeekHeight = customHeights.minByOrNull { abs(it - newPeekHeight) } ?: newPeekHeight
                    bottomSheetBehavior.peekHeight = newPeekHeight
                    Log.d("taejung", "Motion 움직임")

                    true
                }
                MotionEvent.ACTION_UP -> {
                    // 드래그가 끝날 때 최종 높이를 다시 조정하여 가장 가까운 단계에 맞추기
                    view.performClick()
                    val endPeekHeight = (bottomSheetBehavior.peekHeight)
                    val closestHeight = customHeights.minByOrNull { abs(it - endPeekHeight) } ?: endPeekHeight
                    bottomSheetBehavior.peekHeight = closestHeight
                    Log.d("taejung", "Motion up")
                    true
                }
                else -> false
            }
        }
    }


    private fun initCameraPosition() {
        val firstMarker = resultDataList.firstOrNull()
        firstMarker?.let {
            val location = LatLng(it.lat, it.lon)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16f))
        }
    }

    private fun getSampleMarker() {
        resultDataList.forEach { resultData ->
            addCustomMarker(resultData)
        }
    }

    private fun updateVisibleMarkers() {
        val bounds = googleMap.projection.visibleRegion.latLngBounds
        val visibleItems = resultDataList.filter {
            bounds.contains(LatLng(it.lat, it.lon))
        }.sortedByDescending { it.score } // 점수 높은 순으로 정렬

        rankingAdapter.submitList(visibleItems.toList())
    }

    private fun addCustomMarker(resultData: SearchResultData) {
        val binding: ItemMarkerViewBinding = ItemMarkerViewBinding.inflate(layoutInflater)
        binding.tvRanking.text = resultData.ranking.toString()
        binding.tvPrice.text = resultData.price

        val markerView = binding.root
        val markerBitmap = createBitmapFromView(markerView)

        val markerOptions = MarkerOptions()
            .position(LatLng(resultData.lat, resultData.lon))
            .icon(BitmapDescriptorFactory.fromBitmap(markerBitmap))

        val marker = googleMap.addMarker(markerOptions)
        marker?.tag = resultData
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
        Log.d("SearchFragment", "Marker clicked: ${marker.position}")
        selectedMarker?.let {
            updateMarkerView(it, false)
        }
        updateMarkerView(marker, true)
        selectedMarker = marker

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.position, 16f))
        return true
    }

    override fun onMapClick(latLng: LatLng) {
        selectedMarker?.let {
            updateMarkerView(it, false)
        }
        selectedMarker = null
    }

    private fun updateMarkerView(marker: Marker, isSelected: Boolean) {
        val markerItem = marker.tag as SearchResultData
        val binding: ItemMarkerViewBinding = ItemMarkerViewBinding.inflate(layoutInflater)

        binding.root.isSelected = isSelected
        binding.tvRanking.text = markerItem.ranking.toString()
        binding.tvPrice.text = markerItem.price

        val newIcon = BitmapDescriptorFactory.fromBitmap(createBitmapFromView(binding.root))
        marker.setIcon(newIcon)
    }

    private fun initDataManager() {
        val token = getJwt()
        Log.d("token",token)
        if(token.isNotEmpty()){
            val authService = AuthService()
            authService.setSearchCompleteView(this)
        }else{
            Log.d("token 오류","token 오류")
        }
    }

    override fun SearchCompleteLoading() {
        TODO("Not yet implemented")
    }

    override fun SearchCompleteSuccess(content: SearchCompleteResponse) {
        SearchResultData(37.5393, 127.0709,"https://cdn.pixabay.com/photo/2021/08/08/14/16/road-6531031_1280.jpg",
            1, 100, "월세", "1,000만원", "100점 짜리", "주차공간 존재"),
        SearchResultData(37.5432, 127.071,"https://cdn.pixabay.com/photo/2019/11/20/14/48/mirror-house-4640243_1280.jpg",
            2, 200, "월세", "1,000만원", "200점 짜리", "주차공간 존재"),
        SearchResultData(37.5413, 127.073,"https://cdn.pixabay.com/photo/2021/08/08/14/16/road-6531031_1280.jpg",
            3, 150, "월세", "1,000만원", "150점 짜리", "주차공간 존재"),
        SearchResultData(37.5373, 127.074,"https://cdn.pixabay.com/photo/2019/11/20/14/48/mirror-house-4640243_1280.jpg",
            4, 50, "월세", "1,000만원", "50점 짜리", "주차공간 존재"),
        SearchResultData(37.5582, 127.0729,"https://cdn.pixabay.com/photo/2021/08/08/14/16/road-6531031_1280.jpg",
            5, 30, "월세", "1,000만원", "30점 짜리", "주차공간 존재"),
        SearchResultData(37.5632, 127.0739,"https://cdn.pixabay.com/photo/2019/11/20/14/48/mirror-house-4640243_1280.jpg",
            6, 20, "월세", "1,000만원", "20점 짜리", "주차공간 존재"),
        SearchResultData(37.5682, 127.0749,"https://cdn.pixabay.com/photo/2021/08/08/14/16/road-6531031_1280.jpg",
            7,700,  "월세", "1,000만원", "700점 짜리", "주차공간 존재"),
        SearchResultData(38.5732, 127.0759,"https://cdn.pixabay.com/photo/2019/11/20/14/48/mirror-house-4640243_1280.jpg",
            8, 280,"월세", "1,000만원", "280점 짜리", "주차공간 존재")

        
    }

    override fun SearchCompleteFailure(status: Int, message: String) {
        TODO("Not yet implemented")
    }

}
