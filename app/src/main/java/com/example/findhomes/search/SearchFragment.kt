package com.example.findhomes.search

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Display
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.MotionEvent
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findhomes.R
import com.example.findhomes.data.MarkerItem
import com.example.findhomes.data.RankingInfo
import com.example.findhomes.data.SearchResultData
import com.example.findhomes.databinding.FragmentSearchBinding
import com.example.findhomes.databinding.ItemMarkerViewBinding
import com.example.findhomes.search.ResultRankingAdapter
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
import kotlin.math.max
import kotlin.math.min

class SearchFragment : Fragment(), OnMapReadyCallback, OnMarkerClickListener, OnMapClickListener{
    lateinit var binding: FragmentSearchBinding
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var rankingAdapter: ResultRankingAdapter
    private var rankingInfo : ArrayList<SearchResultData> = arrayListOf()
    private var dummyList : ArrayList<MarkerItem> = arrayListOf()
    private var selectedMarker: Marker? = null

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
        initStatisticFragment()

        return binding.root
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
        rankingAdapter = ResultRankingAdapter(rankingInfo, requireContext())

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
        rankingInfo.addAll(
            arrayListOf(
                SearchResultData("https://cdn.pixabay.com/photo/2021/08/08/14/16/road-6531031_1280.jpg",
                    1, "월세", "1,000만원", "3개", "주차공간 존재"),
                SearchResultData("https://cdn.pixabay.com/photo/2019/11/20/14/48/mirror-house-4640243_1280.jpg",
                    2, "월세", "1,000만원", "3개", "주차공간 존재"),
                SearchResultData("https://cdn.pixabay.com/photo/2021/08/08/14/16/road-6531031_1280.jpg",
                    3, "월세", "1,000만원", "3개", "주차공간 존재"),
                SearchResultData("https://cdn.pixabay.com/photo/2019/11/20/14/48/mirror-house-4640243_1280.jpg",
                    4, "월세", "1,000만원", "3개", "주차공간 존재"),
                SearchResultData("https://cdn.pixabay.com/photo/2021/08/08/14/16/road-6531031_1280.jpg",
                    5, "월세", "1,000만원", "3개", "주차공간 존재"),
                SearchResultData("https://cdn.pixabay.com/photo/2019/11/20/14/48/mirror-house-4640243_1280.jpg",
                    6, "월세", "1,000만원", "3개", "주차공간 존재"),
                SearchResultData("https://cdn.pixabay.com/photo/2021/08/08/14/16/road-6531031_1280.jpg",
                    7, "월세", "1,000만원", "3개", "주차공간 존재"),
                SearchResultData("https://cdn.pixabay.com/photo/2019/11/20/14/48/mirror-house-4640243_1280.jpg",
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

        getSampleMarker()
        initCameraPosition()
    }

    private fun initCameraPosition() {
        val firstMarker = dummyList.firstOrNull { it.ranking == 1 }
        firstMarker?.let {
            val location = LatLng(it.lat, it.lon)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16f))
        }
    }

    private fun getSampleMarker() {
        dummyList.addAll(
            arrayListOf(
                MarkerItem(37.5393, 127.0709, 1, "1,000만원"),
                MarkerItem(37.5432, 127.071, 2, "2,000만원"),
                MarkerItem(37.5482, 127.0709, 3, "3,000만원"),
                MarkerItem(37.5363, 127.0711, 4, "4,000만원")
                )
        )

        dummyList.forEach { markerItem ->
            addCustomMarker(markerItem)
        }
    }

    private fun addCustomMarker(markerItem: MarkerItem) {
        val binding: ItemMarkerViewBinding = ItemMarkerViewBinding.inflate(layoutInflater)
        binding.tvRanking.text = markerItem.ranking.toString()
        binding.tvPrice.text = markerItem.price

        val markerView = binding.root
        applyNormalStyle(binding)
        val markerBitmap = createBitmapFromView(markerView)

        val markerOptions = MarkerOptions()
            .position(LatLng(markerItem.lat, markerItem.lon))
            .icon(BitmapDescriptorFactory.fromBitmap(markerBitmap))

        val marker = googleMap.addMarker(markerOptions)
        marker?.tag = markerItem
    }

    private fun applySelectedStyle(binding: ItemMarkerViewBinding) {
        binding.tvPrice.setTextColor(Color.BLACK)
    }

    private fun applyNormalStyle(binding: ItemMarkerViewBinding) {
//        binding.tvPrice.setTextColor(Color.WHITE)
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
        selectedMarker?.let {
            updateMarkerView(it, false)
        }

        updateMarkerView(marker, true)
        selectedMarker = marker
        return true
    }

    override fun onMapClick(latLng: LatLng) {
        selectedMarker?.let {
            updateMarkerView(it, false)
        }
        selectedMarker = null
    }

    private fun updateMarkerView(marker: Marker, isSelected: Boolean) {
        val markerItem = marker.tag as MarkerItem
        val binding: ItemMarkerViewBinding = ItemMarkerViewBinding.inflate(layoutInflater)
        binding.tvRanking.text = markerItem.ranking.toString()
        binding.tvPrice.text = markerItem.price

        if (isSelected) {
            applySelectedStyle(binding)
        } else {
            applyNormalStyle(binding)
        }

        val newIcon = BitmapDescriptorFactory.fromBitmap(createBitmapFromView(binding.root))
        marker.setIcon(newIcon)
    }


}
