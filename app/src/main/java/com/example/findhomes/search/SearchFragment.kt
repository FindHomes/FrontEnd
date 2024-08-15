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
import androidx.recyclerview.widget.RecyclerView
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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        initDataManager()

        initMap(savedInstanceState)
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

    private fun updateMap(position: Int) {
        resultDataList.getOrNull(position)?.let {
            val location = LatLng(it.lon, it.lat)
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 17f))
            selectedMarker = markerMap[position]
            updateMarkerView(selectedMarker, true)
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
        binding.rvResultRanking.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.rvResultRanking.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val position = layoutManager.findFirstCompletelyVisibleItemPosition()
                    if (position != RecyclerView.NO_POSITION) {
                        updateMap(position)
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
        val firstMarker = resultDataList.firstOrNull()
        Log.d("firstMarker", firstMarker.toString())
        firstMarker?.let {
            val location = LatLng(it.lon, it.lat)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 17f))
        }
    }

    private fun getSampleMarker() {
        resultDataList.forEachIndexed { index, resultData ->
            addCustomMarker(resultData, index)
        }
    }

    private fun updateVisibleMarkers() {
        val bounds = googleMap.projection.visibleRegion.latLngBounds
        val visibleItems = resultDataList.filter {
            bounds.contains(LatLng(it.lon, it.lat))
        }.sortedByDescending { it.score }

        rankingAdapter.submitList(visibleItems.toList())
    }

    private fun addCustomMarker(resultData: SearchResultData, index: Int) {
        val binding: ItemMarkerViewBinding = ItemMarkerViewBinding.inflate(layoutInflater)
        binding.tvRanking.text = (index + 1).toString()
        binding.tvPrice.text = resultData.price

        val markerView = binding.root
        val markerBitmap = createBitmapFromView(markerView)

        val markerOptions = MarkerOptions()
            .position(LatLng(resultData.lon, resultData.lat))
            .icon(BitmapDescriptorFactory.fromBitmap(markerBitmap))

        val marker = googleMap.addMarker(markerOptions)
        marker?.tag = resultData
        markerMap[index] = marker!!
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

    private fun updateMarkerView(marker: Marker?, isSelected: Boolean) {
        marker?.let {
            val markerItem = it.tag as SearchResultData
            val binding: ItemMarkerViewBinding = ItemMarkerViewBinding.inflate(layoutInflater)

            binding.root.isSelected = isSelected
            binding.tvRanking.text = markerItem.price
            binding.tvPrice.text = markerItem.price

            val newIcon = BitmapDescriptorFactory.fromBitmap(createBitmapFromView(binding.root))
            it.setIcon(newIcon)
        }
    }

    private fun initDataManager() {
//        val token = getJwt()
//        Log.d("token",token)
//        if(token.isNotEmpty()){
//            val authService = AuthService()
//            authService.setSearchCompleteView(this)
//        }else{
//            Log.d("token 오류","token 오류")
//        }

        val authService = AuthService()
        authService.setSearchCompleteView(this)
        authService.searchComplete()
    }

    override fun SearchCompleteLoading() {
        TODO("Not yet implemented")
    }

    override fun SearchCompleteSuccess(content: SearchCompleteResponse) {
        resultDataList.clear()
        resultDataList.addAll(content.houses.mapIndexed { index, house ->
            SearchResultData(
                houseId = house.houseId,
                price = house.price.toString(),
                priceType = house.priceType,
                room = house.roomNum.toString(),
                washRoom = house.washroomNum.toString(),
                img = house.url,
                lat = house.x,
                lon = house.y,
                score = house.score.toInt()
            ).also {
                addCustomMarker(it, index)
            }
        })
    }

    override fun SearchCompleteFailure() {
        Log.d("fail", "fail")
    }

}
