package com.example.findhomes.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findhomes.data.RankingInfo
import com.example.findhomes.data.SearchResultData
import com.example.findhomes.databinding.FragmentSearchBinding
import com.example.findhomes.search.ResultRankingAdapter
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlin.math.max
import kotlin.math.min

class SearchFragment : Fragment(), OnMapReadyCallback {
    lateinit var binding: FragmentSearchBinding
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var rankingAdapter: ResultRankingAdapter
    private var rankingInfo : ArrayList<SearchResultData> = arrayListOf()

    private var initialTouchY: Float = 0f
    private var initialPeekHeight: Int = 0
    private val minHeight by lazy { resources.displayMetrics.heightPixels / 4 }
    private val midHeight by lazy { resources.displayMetrics.heightPixels / 2 }
    private val maxHeight by lazy { resources.displayMetrics.heightPixels }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)

        initMapView(savedInstanceState)
        setupBottomSheet()
        initData()
        initRankingRecyclerView()

        return binding.root
    }

    private fun initRankingRecyclerView() {
        rankingAdapter = ResultRankingAdapter(rankingInfo, requireContext())

        binding.rvResultRanking.adapter = rankingAdapter
        binding.rvResultRanking.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

    }

    private fun initData() {
        rankingInfo.addAll(
            arrayListOf(
                SearchResultData("https://www.google.com/url?sa=i&url=https%3A%2F%2Fblog.naver.com%2Fgmlghks0810%2F220600327050&psig=AOvVaw22-z9NpaK8yUj9GB8g4t4r&ust=1721734992036000&source=images&cd=vfe&opi=89978449&ved=0CBEQjRxqFwoTCKjnrefIuocDFQAAAAAdAAAAABAE",
                    1, "월세", "1,000만원", "3개", "주차공간 존재"),
                SearchResultData("https://www.google.com/url?sa=i&url=https%3A%2F%2Fblog.naver.com%2Fgmlghks0810%2F220600327050&psig=AOvVaw22-z9NpaK8yUj9GB8g4t4r&ust=1721734992036000&source=images&cd=vfe&opi=89978449&ved=0CBEQjRxqFwoTCKjnrefIuocDFQAAAAAdAAAAABAE",
                    2, "월세", "1,000만원", "3개", "주차공간 존재"),
                SearchResultData("https://www.google.com/url?sa=i&url=https%3A%2F%2Fblog.naver.com%2Fgmlghks0810%2F220600327050&psig=AOvVaw22-z9NpaK8yUj9GB8g4t4r&ust=1721734992036000&source=images&cd=vfe&opi=89978449&ved=0CBEQjRxqFwoTCKjnrefIuocDFQAAAAAdAAAAABAE",
                    3, "월세", "1,000만원", "3개", "주차공간 존재"),
                SearchResultData("https://www.google.com/url?sa=i&url=https%3A%2F%2Fblog.naver.com%2Fgmlghks0810%2F220600327050&psig=AOvVaw22-z9NpaK8yUj9GB8g4t4r&ust=1721734992036000&source=images&cd=vfe&opi=89978449&ved=0CBEQjRxqFwoTCKjnrefIuocDFQAAAAAdAAAAABAE",
                    4, "월세", "1,000만원", "3개", "주차공간 존재"),
                SearchResultData("https://www.google.com/url?sa=i&url=https%3A%2F%2Fblog.naver.com%2Fgmlghks0810%2F220600327050&psig=AOvVaw22-z9NpaK8yUj9GB8g4t4r&ust=1721734992036000&source=images&cd=vfe&opi=89978449&ved=0CBEQjRxqFwoTCKjnrefIuocDFQAAAAAdAAAAABAE",
                    5, "월세", "1,000만원", "3개", "주차공간 존재"),
                SearchResultData("https://www.google.com/url?sa=i&url=https%3A%2F%2Fblog.naver.com%2Fgmlghks0810%2F220600327050&psig=AOvVaw22-z9NpaK8yUj9GB8g4t4r&ust=1721734992036000&source=images&cd=vfe&opi=89978449&ved=0CBEQjRxqFwoTCKjnrefIuocDFQAAAAAdAAAAABAE",
                    6, "월세", "1,000만원", "3개", "주차공간 존재"),
                SearchResultData("https://www.google.com/url?sa=i&url=https%3A%2F%2Fblog.naver.com%2Fgmlghks0810%2F220600327050&psig=AOvVaw22-z9NpaK8yUj9GB8g4t4r&ust=1721734992036000&source=images&cd=vfe&opi=89978449&ved=0CBEQjRxqFwoTCKjnrefIuocDFQAAAAAdAAAAABAE",
                    7, "월세", "1,000만원", "3개", "주차공간 존재"),
                SearchResultData("https://www.google.com/url?sa=i&url=https%3A%2F%2Fblog.naver.com%2Fgmlghks0810%2F220600327050&psig=AOvVaw22-z9NpaK8yUj9GB8g4t4r&ust=1721734992036000&source=images&cd=vfe&opi=89978449&ved=0CBEQjRxqFwoTCKjnrefIuocDFQAAAAAdAAAAABAE",
                    8, "월세", "1,000만원", "3개", "주차공간 존재"),
            )
        )
    }

    private fun initMapView(savedInstanceState: Bundle?) {
        mapView = binding.mvRanking
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    private fun setupBottomSheet() {
        val bottomSheet = binding.clBottomBar
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.isDraggable = false
        bottomSheetBehavior.peekHeight = midHeight

        val viewHandler = binding.clBottomHandler
        viewHandler.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialTouchY = event.rawY
                    initialPeekHeight = bottomSheetBehavior.peekHeight
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val deltaY = event.rawY - initialTouchY
                    val newPeekHeight = (initialPeekHeight - deltaY).toInt().coerceIn(minHeight, maxHeight)
                    bottomSheetBehavior.peekHeight = newPeekHeight
                    true
                }
                MotionEvent.ACTION_UP -> {
                    true
                }
                else -> false
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }
}
