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
import com.example.findhomes.data.model.SearchCompleteResponse
import com.example.findhomes.databinding.FragmentSearchBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.clustering.Clusterer
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
    private lateinit var clusterer: Clusterer<ItemKey>
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

        // BottomSheet 설정
        val bottomSheet = binding.rvResultRanking
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        observeViewModel()
        initRecyclerView()
        initMoreButton()

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

        viewModel.canLoadMore.observe(viewLifecycleOwner) { canLoad ->
            binding.btnMore.isEnabled = canLoad
        }
    }

    private fun initClusterer() {
        clusterer = Clusterer.Builder<ItemKey>()
            .screenDistance(20.0)  // dp 단위의 클러스터링 거리
            .minZoom(4)
            .maxZoom(16)
            .build()
        clusterer.map = naverMap
    }

    private fun updateMapData(searchData: List<SearchCompleteResponse>?) {
        searchData ?: return  // searchData가 null인 경우 함수를 종료

        // 현재 보여줄 최대 인덱스까지의 매물 데이터
        val housesToShow = searchData.take(viewModel.currentMaxIndex)

        // 클러스터러에 기존 아이템 제거
        clusterer.clear()

        // 새로운 아이템을 클러스터러에 추가
        searchData.forEachIndexed { index, house ->
            val itemKey = ItemKey(index, LatLng(house.y, house.x))
            clusterer.add(itemKey, null)
        }

        // RecyclerView와 마커 업데이트
        rankingAdapter.submitList(housesToShow)
        updateMarkers(housesToShow)
        updateMapBounds(housesToShow)
    }

    private fun createCustomMarkerView(house: SearchCompleteResponse): View {
        val view = LayoutInflater.from(context).inflate(R.layout.item_marker_view, null)
        view.findViewById<TextView>(R.id.tv_price_type).text = house.priceType
//        view.findViewById<TextView>(R.id.tv_ranking).text = house .toString()
        view.findViewById<TextView>(R.id.tv_price).text = "${house.price}만원"
        return view
    }


    private fun initMoreButton() {
        binding.btnMore.setOnClickListener {
            viewModel.loadMoreData()
        }
    }

    private fun updateMapBounds(houses: List<SearchCompleteResponse>) {
        if (houses.isEmpty()) return

        val boundsBuilder = LatLngBounds.Builder()
        houses.forEach { house ->
            boundsBuilder.include(LatLng(house.y, house.x))
        }
        val bounds = boundsBuilder.build()

        naverMap.moveCamera(CameraUpdate.fitBounds(bounds, 100))  // 100은 여백을 의미
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
            override fun onItemClicked(data: SearchCompleteResponse) {
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
    private fun updateMarkers(houses: List<SearchCompleteResponse>?) {
        markerMap.clear()
        houses?.forEachIndexed { index, house ->
            val viewMarker = LayoutInflater.from(context).inflate(R.layout.item_marker_view, null)
            val tvPriceType = viewMarker.findViewById<TextView>(R.id.tv_price_type)
            val tvRanking = viewMarker.findViewById<TextView>(R.id.tv_ranking)
            val tvPrice = viewMarker.findViewById<TextView>(R.id.tv_price)
            tvPriceType.text = house.priceType
            tvRanking.text = (index + 1).toString()
            tvPrice.text = house.price.toString()

            val bitmap = createBitmapFromView(viewMarker)
            val marker = Marker().apply {
                position = LatLng(house.y, house.x)
                map = naverMap
                icon = OverlayImage.fromBitmap(bitmap)
                setOnClickListener {
                    binding.rvResultRanking.scrollToPosition(index)
                    updateMap(index)
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
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
        initClusterer()
        naverMap.setOnMapClickListener { _, _ ->
            Log.d("SearchFragment", "Map clicked.")
            deselectMarkers()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            // 모든 마커의 선택 해제
            Log.d("bottom", bottomSheetBehavior.state.toString())
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
