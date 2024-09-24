package com.example.findhomes.presentation.ui.search

import android.annotation.SuppressLint
import android.content.ClipData.Item
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.support.v4.os.IResultReceiver.Default
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
import com.example.findhomes.databinding.ItemMarkerViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.clustering.ClusterMarkerInfo
import com.naver.maps.map.clustering.Clusterer
import com.naver.maps.map.clustering.DefaultClusterMarkerUpdater
import com.naver.maps.map.clustering.DefaultLeafMarkerUpdater
import com.naver.maps.map.clustering.LeafMarkerInfo
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
    private var itemPositionMap = mutableMapOf<Int, LatLng>()
    private lateinit var clusterer: Clusterer<ItemKey>
    private lateinit var markerBinding: ItemMarkerViewBinding
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        markerBinding = ItemMarkerViewBinding.inflate(layoutInflater)
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

    private fun updateMapData(searchData: List<SearchCompleteResponse>?) {
        searchData ?: return  // searchData가 null인 경우 함수를 종료

        // 현재 보여줄 최대 인덱스까지의 매물 데이터
        val housesToShow = searchData.take(viewModel.currentMaxIndex)

        // RecyclerView와 마커 업데이트
        rankingAdapter.submitList(housesToShow)
        updateClusterer(housesToShow)
    }

    private fun updateClusterer(housesToShow: List<SearchCompleteResponse>) {
        val builder = Clusterer.ComplexBuilder<ItemKey>().apply {
            leafMarkerUpdater(object : DefaultLeafMarkerUpdater() {
                override fun updateLeafMarker(info: LeafMarkerInfo, marker: Marker) {
                    super.updateLeafMarker(info, marker)
                    val item = (info.key as ItemKey).searchCompleteResponse
                    markerBinding.tvPrice.text = item.price.toString()
                    markerBinding.tvPriceType.text = item.priceType

                    itemPositionMap[item.houseId] = marker.position
                    marker.setOnClickListener {
                        val position = housesToShow.indexOfFirst { it.houseId == item.houseId }
                        binding.rvResultRanking.scrollToPosition(position)
                        true
                    }

                    val bitmap = createBitmapFromView(markerBinding.root)
                    marker.icon = OverlayImage.fromBitmap(bitmap)
                }
            })
        }

        clusterer = builder.build()
        clusterer.map = naverMap

        addItemsToClusterer(housesToShow)
    }

    private fun createBitmapFromView(view: View): Bitmap {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun addItemsToClusterer(searchData: List<SearchCompleteResponse>) {
        val keyTagMap = searchData.map { data ->
            ItemKey(data, LatLng(data.y, data.x)) to data
        }.toMap()

        clusterer.addAll(keyTagMap)
    }


    private fun initMoreButton() {
        binding.btnMore.setOnClickListener {
            viewModel.loadMoreData()
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
                    val position = (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                    if (position != RecyclerView.NO_POSITION) {
                        val item = rankingAdapter.getItemAtPosition(position)
                        itemPositionMap[item.houseId]?.let {
                            naverMap.moveCamera(CameraUpdate.scrollTo(it))
                        }
                    }
                }
            }
        })

        rankingAdapter.setOnItemClickListener(object : ResultRankingAdapter.OnItemClickListener{
            override fun onItemClicked(data: SearchCompleteResponse) {
                val bundle = Bundle()
                bundle.putInt("houseId", data.houseId)

                val nextFragment = SearchDetailFragment()
                nextFragment.arguments = bundle
                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, nextFragment)
                    .commit()
            }
        })
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
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
