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
import androidx.core.content.ContextCompat
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
    private var itemPositionMap = mutableMapOf<Int, LatLng>()
    private lateinit var clusterer: Clusterer<ItemKey>
    private var selectedMarker: Marker? = null
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

        val housesToShow = searchData.take(viewModel.currentMaxIndex)
        housesToShow.forEach {
            itemPositionMap[it.houseId] = LatLng(it.y, it.x)
        }

        rankingAdapter.submitList(housesToShow)
        updateClusterer(housesToShow)
        initCameraBounds(housesToShow)
    }

    private fun initCameraBounds(housesToShow: List<SearchCompleteResponse>) {
        if (housesToShow.isEmpty()) return

        val boundsBuilder = LatLngBounds.Builder()
        housesToShow.forEach {
            boundsBuilder.include(LatLng(it.y, it.x))
        }
        val bounds = boundsBuilder.build()

        val cameraUpdate = CameraUpdate.fitBounds(bounds, 100)
        naverMap.moveCamera(cameraUpdate)
    }

    private fun updateClusterer(housesToShow: List<SearchCompleteResponse>) {
        // 클러스터러 초기화 전 클러스터러가 올바르게 설정되었는지 확인
        if (!::clusterer.isInitialized) {
            Log.d("clusterer","clusterer error")
        } else {
            clusterer.clear()  // 이전 데이터 클리어
        }

        val builder = Clusterer.ComplexBuilder<ItemKey>().apply {
            leafMarkerUpdater(object : DefaultLeafMarkerUpdater() {
                override fun updateLeafMarker(info: LeafMarkerInfo, marker: Marker) {
                    super.updateLeafMarker(info, marker)
                    val item = (info.key as ItemKey).searchCompleteResponse
                    markerBinding.tvPrice.text = formatPrice(item.price)
                    markerBinding.tvPriceType.text = item.priceType
                    markerBinding.tvRanking.text = item.ranking.toString()

                    marker.tag = ItemKey(item, marker.position)
                    val bitmap = createBitmapFromView(markerBinding.root)
                    marker.icon = OverlayImage.fromBitmap(bitmap)

                    if (item.ranking == 1 && selectedMarker == null) {
                        // 특정 조건에 따라 초기 선택된 마커 설정
                        updateMarkerAppearance(marker, true, item)
                        selectedMarker = marker
                    } else {
                        marker.setOnClickListener {
                            // 클릭된 마커의 정보와 선택 상태를 업데이트
                            updateMarkerAppearance(marker, true, item)
                            if (selectedMarker != null && selectedMarker != marker) {
                                updateMarkerAppearance(selectedMarker!!, false, (selectedMarker!!.tag as ItemKey).searchCompleteResponse)
                            }
                            selectedMarker = marker
                            val position = housesToShow.indexOfFirst { it.houseId == item.houseId }
                            binding.rvResultRanking.scrollToPosition(position)
                            true
                        }
                    }
                }
            })
        }

        clusterer = builder.build()
        clusterer.map = naverMap
        addItemsToClusterer(housesToShow)
    }

    private fun updateMarkerAppearance(marker: Marker, isSelected: Boolean, item: SearchCompleteResponse) {
        // 마커 뷰 설정
        markerBinding.tvPrice.text = formatPrice(item.price)
        markerBinding.tvPriceType.text = item.priceType
        markerBinding.tvRanking.text = item.ranking.toString()

        // 배경색과 텍스트 색상 변경
        markerBinding.clRankingInfo.isSelected = isSelected
        markerBinding.cvRanking.isSelected = isSelected
        markerBinding.tvPriceType.isSelected = isSelected
        markerBinding.tvPriceCenter.isSelected = isSelected
        markerBinding.tvPrice.isSelected = isSelected

        val bitmap = createBitmapFromView(markerBinding.root)
        marker.icon = OverlayImage.fromBitmap(bitmap)
    }

    private fun formatPrice(price: Int): String {
        return when {
            price >= 10000 -> {
                val billions = price / 10000
                val remainder = price % 10000
                if (remainder == 0) "${billions}억"
                else "${billions}억 ${remainder}만원"
            }
            else -> "${price}만원"
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
                    Log.d("position", position.toString())
                    if (position != RecyclerView.NO_POSITION) {
                        val item = rankingAdapter.getItemAtPosition(position)
                        itemPositionMap[item.houseId]?.let {
                            naverMap.moveCamera(CameraUpdate.scrollTo(it))
                            Log.d("positionLatLng", it.toString())
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
                    .addToBackStack(null)
                    .commit()
            }
        })
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
    }
}
