package com.example.findhomes.presentation.ui.search

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.findhomes.R
import com.example.findhomes.data.model.HousesResponse
import com.example.findhomes.data.model.SearchCompleteResponse
import com.example.findhomes.data.model.SearchResultData
import com.example.findhomes.databinding.FragmentSearchBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.MarkerIcons
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var naverMap: NaverMap
    private lateinit var rankingAdapter: ResultRankingAdapter
    private var selectedMarker: Marker? = null
    private var markerMap = mutableMapOf<Int, Marker>()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private val viewModel: SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.mvRanking.onCreate(savedInstanceState)
        binding.mvRanking.getMapAsync(this)

        viewModel.loadSearchData()

        // BottomSheet 설정
        val bottomSheet = binding.rvResultRanking
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        observeViewModel()
        initRecyclerView()

        return binding.root
    }

    private fun observeViewModel() {
        viewModel.searchData.observe(viewLifecycleOwner) { searchData ->
            if (!::naverMap.isInitialized) {
                binding.mvRanking.getMapAsync {
                    naverMap = it
                    updateMapData(searchData) // 맵 데이터 업데이트 함수 호출
                }
            } else {
                updateMapData(searchData)
            }
        }
    }

    private fun updateMapData(searchData: SearchCompleteResponse?) {
        if (searchData == null) return
        // RecyclerView와 마커 업데이트
        rankingAdapter.submitList(searchData.houses)
        updateMarkers(searchData.houses)

        // 마커와 지도 경계 업데이트
        if (searchData.houses.isNotEmpty()) {
            updateMapBounds(searchData.xmax, searchData.xmin, searchData.ymax, searchData.ymin)
        }
    }

    private fun updateMapBounds(xmax: Double, xmin: Double, ymax: Double, ymin: Double) {
        val bounds = LatLngBounds(
            LatLng(ymin, xmin),  // 남서쪽 코너
            LatLng(ymax, xmax)   // 북동쪽 코너
        )
        naverMap.moveCamera(CameraUpdate.fitBounds(bounds))
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
                    val position = (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                    if (position != RecyclerView.NO_POSITION) {
                        updateMap(position)
                    }
                }
            }
        })

        rankingAdapter.setOnItemClickListener(object : ResultRankingAdapter.OnItemClickListener{
            override fun onItemClicked(data: HousesResponse) {
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


    private fun createBitmapFromView(view: View): Bitmap {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    @SuppressLint("SetTextI18n")
    private fun updateMarkers(houses: List<HousesResponse>?) {
        markerMap.clear()
        houses?.forEachIndexed { index, house ->
            val viewMarker = LayoutInflater.from(context).inflate(R.layout.item_marker_view, null)
            val tvRanking = viewMarker.findViewById<TextView>(R.id.tv_ranking)
            val tvPrice = viewMarker.findViewById<TextView>(R.id.tv_price)
            tvRanking.text = (index + 1).toString()
            tvPrice.text = "${house.price}만원"

            val bitmap = createBitmapFromView(viewMarker)
            val marker = Marker().apply {
                position = LatLng(house.y, house.x)
                map = naverMap
                icon = OverlayImage.fromBitmap(bitmap)
                setOnClickListener {
                    binding.rvResultRanking.scrollToPosition(index)
                    updateMap(index)
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    true
                }
            }
            markerMap[index] = marker
        }
    }


    private fun updateMap(position: Int) {
        val house = markerMap[position]?.position
        house?.let {
            naverMap.moveCamera(CameraUpdate.scrollTo(it))
            selectedMarker?.map = null
            selectedMarker = markerMap[position]
            selectedMarker?.map = naverMap
        }
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        naverMap.setOnMapClickListener { _, _ ->
            Log.d("SearchFragment", "Map clicked.")
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            // 모든 마커의 선택 해제
            deselectMarkers()
        }
    }

    private fun deselectMarkers() {
        selectedMarker?.let {
            it.map = null  // 마커를 지도에서 제거
            it.map = naverMap  // 다시 마커를 지도에 추가하지만, 선택은 해제된 상태
        }
        selectedMarker = null
    }
}
