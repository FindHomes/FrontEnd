package com.example.findhomes.presentation.ui.search

import com.example.findhomes.data.model.SearchCompleteResponse
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.clustering.ClusteringKey

class ItemKey(val searchCompleteResponse: SearchCompleteResponse, private val position: LatLng) : ClusteringKey {
    val id = searchCompleteResponse.houseId

    override fun getPosition() = position

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val itemKey = other as ItemKey
        return id == itemKey.id
    }

    override fun hashCode() = id
}