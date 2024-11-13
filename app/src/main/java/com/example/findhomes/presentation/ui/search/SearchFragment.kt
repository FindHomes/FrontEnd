package com.example.findhomes.presentation.ui.search

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.findhomes.R
import com.example.findhomes.data.model.ManConRequest
import com.example.findhomes.data.model.SearchCompleteResponse
import com.example.findhomes.databinding.FragmentSearchBinding
import com.example.findhomes.databinding.ItemMarkerViewBinding
import com.example.findhomes.databinding.ItemSelectedMarkerViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.clustering.Clusterer
import com.naver.maps.map.clustering.DefaultLeafMarkerUpdater
import com.naver.maps.map.clustering.LeafMarkerInfo
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var naverMap: NaverMap
    private var itemPositionMap = mutableMapOf<Int, LatLng>()
    private var aloneMarkerMap = mutableMapOf<Int, Marker>()
    private lateinit var clusterer: Clusterer<ItemKey>
    private var selectedMarker: Marker? = null
    private var selectedAloneMarker: Marker? = null
    private lateinit var aloneMarkerBinding: ItemSelectedMarkerViewBinding
    private lateinit var markerBinding: ItemMarkerViewBinding
    private lateinit var rankingAdapter: ResultRankingAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private val viewModel: SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val manConRequest = arguments?.getSerializable("manConRequest") as? ManConRequest
        val searchLogId = arguments?.getInt("searchLogId")
        Log.d("manConRequest",manConRequest.toString())
        Log.d("searchLogId호출",searchLogId.toString())


        if (manConRequest != null) {
            Log.d("manConRequest호출","manConRequest호출")
            binding.searchClNone.visibility = View.GONE
            showLoadingAnimation(true)
            viewModel.loadSearchData(manConRequest)
        } else if (searchLogId != null) {
            Log.d("searchLogId호출","searchLogId호출")
            binding.searchClNone.visibility = View.GONE
            showLoadingAnimation(true)
            viewModel.loadSearchLogData(searchLogId)
        } else {
            binding.searchClMain.visibility = View.GONE
            binding.searchClNone.visibility = View.VISIBLE
            binding.searchLa.visibility = View.GONE
        }

        binding.mvRanking.onCreate(savedInstanceState)
        binding.mvRanking.getMapAsync(this)

        // BottomSheet 설정
        val bottomSheet = binding.rvResultRanking
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        observeViewModel()
        initRecyclerView()
        initMoreButton()
        initDetailButton()
        initStatistics()
        initSearchLogs()

        return binding.root
    }

    private fun initSearchLogs() {
        binding.btnRegisterLog.setOnClickListener{
            viewModel.postSearchLogsData()
            Toast.makeText(requireContext(),"검색 조건이 저장되었습니다! 찜 목록에서 확인해주세요", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initStatistics() {
        binding.btnStatisticShow.setOnClickListener{
//            StatisticsFragment().arguments = Bundle()
//            parentFragmentManager.beginTransaction()
//                .replace(R.id.main_frm, StatisticsFragment())
//                .addToBackStack(null)
//                .commit()
            val intent = Intent(requireContext(), StatisticsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initDetailButton() {
        binding.btnDetailShow.setOnClickListener {
            selectedAloneMarker?.let { marker ->
                val cameraPosition = CameraPosition(marker.position, 16.0, 90.0, 0.0)
                naverMap.moveCamera(CameraUpdate.toCameraPosition(cameraPosition))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        markerBinding = ItemMarkerViewBinding.inflate(layoutInflater)
        aloneMarkerBinding = ItemSelectedMarkerViewBinding.inflate(layoutInflater)
    }

    private fun observeViewModel() {
        viewModel.searchData.observe(viewLifecycleOwner) { searchData ->
            showLoadingAnimation(false)
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

    private fun showLoadingAnimation(show: Boolean) {
        binding.searchLa.visibility = if (show) View.VISIBLE else View.GONE
        binding.searchClMain.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun updateMapData(searchData: List<SearchCompleteResponse>?) {
        searchData ?: return  // searchData가 null인 경우 함수를 종료

        val housesToShow = searchData.take(viewModel.currentMaxIndex)
        housesToShow.forEach {
            itemPositionMap[it.ranking] = LatLng(it.y, it.x)
        }

        rankingAdapter.submitList(housesToShow)
        updateClusterer(housesToShow)
        updateCameraBounds(housesToShow)

        if(housesToShow.isNotEmpty()){
            selectFirstItem(housesToShow.first())
        }
    }

    private fun selectFirstItem(item: SearchCompleteResponse) {
        val firstMarker = aloneMarkerMap[item.ranking]
        firstMarker?.let { marker ->
            marker.isVisible = true
            selectedAloneMarker = marker
        }
    }


    private fun updateCameraBounds(housesToShow: List<SearchCompleteResponse>) {
        if (housesToShow.isEmpty()) return

        val boundsBuilder = LatLngBounds.Builder()
        housesToShow.forEach {
            boundsBuilder.include(LatLng(it.y, it.x))
        }
        val bounds = boundsBuilder.build()

        val cameraUpdate = CameraUpdate.fitBounds(bounds, 200)
        naverMap.moveCamera(cameraUpdate)
    }

    private fun updateClusterer(housesToShow: List<SearchCompleteResponse>) {
        // 클러스터러 초기화 전 클러스터러가 올바르게 설정되었는지 확인
        if (!::clusterer.isInitialized) {
            Log.d("clusterer","clusterer error")
        } else {
            clusterer.clear()  // 이전 데이터 클리어
        }

        housesToShow.forEach { item ->
            createAloneMarker(item)
        }

        val builder = Clusterer.ComplexBuilder<ItemKey>().apply {
            leafMarkerUpdater(object : DefaultLeafMarkerUpdater() {
                override fun updateLeafMarker(info: LeafMarkerInfo, marker: Marker) {
                    super.updateLeafMarker(info, marker)
                    val item = (info.key as ItemKey).searchCompleteResponse

                    createMarker(item, marker)
                    marker.setOnClickListener {
                        selectItem(item)
                        true

                    }
                }

            })
        }

        clusterer = builder.build()
        clusterer.map = naverMap
        addItemsToClusterer(housesToShow)
    }

    private fun selectItem(item: SearchCompleteResponse) {
        // 마커 가시성 조정
        selectedMarker?.isVisible = false
        selectedAloneMarker?.isVisible = false

        selectedAloneMarker = aloneMarkerMap[item.ranking]?.apply {
            isVisible = true
            map = naverMap
        }

        // RecyclerView 스크롤
        val position = rankingAdapter.currentList.indexOfFirst { it.ranking == item.ranking }
        if (position != RecyclerView.NO_POSITION) {
            binding.rvResultRanking.scrollToPosition(position)
        }
    }

    private fun createMarker(item: SearchCompleteResponse, marker: Marker) {
        markerBinding.tvPrice.text = formatPrice(item.price)
        markerBinding.tvPriceType.text = item.priceType
        markerBinding.tvRanking.text = item.ranking.toString()

        marker.tag = ItemKey(item, marker.position)

        val bitmap = createBitmapFromView(markerBinding.root)
        marker.icon = OverlayImage.fromBitmap(bitmap)
    }

    private fun createAloneMarker(item: SearchCompleteResponse) {
        aloneMarkerBinding.tvPrice.text = formatPrice(item.price)
        aloneMarkerBinding.tvPriceType.text = item.priceType
        aloneMarkerBinding.tvRanking.text = item.ranking.toString()

        val marker = Marker().apply {
            zIndex = 100
            position = LatLng(item.y, item.x)
            icon = OverlayImage.fromBitmap(createBitmapFromView(aloneMarkerBinding.root))
            isVisible = false
            map = naverMap
        }

        aloneMarkerMap[item.ranking] = marker
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
                        naverMap.moveCamera(CameraUpdate.scrollTo(LatLng(item.y, item.x)))
                        selectItem(item)
                    }
                }
            }
        })

        rankingAdapter.setOnItemClickListener(object : ResultRankingAdapter.OnItemClickListener{
            override fun onItemClicked(data: SearchCompleteResponse) {
                val intent = Intent(requireContext(), SearchDetailActivity::class.java)
                intent.putExtra("houseId", data.houseId)
                startActivity(intent)
            }
        })

        rankingAdapter.setOnHeartClickListener(object: ResultRankingAdapter.OnHeartClickListener{
            override fun onHeartClicked(data: SearchCompleteResponse) {
                viewModel.postFavoriteData(data.houseId, if(data.favorite) "remove" else "add")
            }
        })
    }

    private fun initMoreButton() {
        binding.btnMore.setOnClickListener {
            viewModel.loadMoreData()
        }
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
    }
}
