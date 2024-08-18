package com.example.findhomes.remote

import com.google.gson.annotations.SerializedName

data class SearchCompleteResponse(
    @SerializedName("houses") val houses: ArrayList<HousesResponse>,
    @SerializedName("xmax") val xmax : Double,
    @SerializedName("xmin") val xmin : Double,
    @SerializedName("ymax") val ymax : Double,
    @SerializedName("ymin") val ymin : Double
)

data class HousesResponse(
    @SerializedName("houseId") val houseId: Int,
    @SerializedName("url") val url: String,
    @SerializedName("priceType") val priceType: String,
    @SerializedName("price") val price: Int,
    @SerializedName("priceForWs") val priceForWs: Int,
    @SerializedName("maintenanceFee") val maintenanceFee: Int,
    @SerializedName("housingType") val housingType : String,
    @SerializedName("isMultiLayer") val isMultiLayer: Boolean,
    @SerializedName("isSeparateType") val isSeparateType : Boolean,
    @SerializedName("floor") val floor: String,
    @SerializedName("size") val size: Double,
    @SerializedName("roomNum") val roomNum: Int,
    @SerializedName("washroomNum") val washroomNum: Int,
    @SerializedName("direction") val direction: String,
    @SerializedName("completionDate") val completionDate: String,
    @SerializedName("houseOption") val houseOption : String,
    @SerializedName("address") val address: String,
    @SerializedName("x") val x: Double,
    @SerializedName("y") val y: Double,
    @SerializedName("imgUrl") val imgUrl: String,
    @SerializedName("score") val score: Double
)

data class SearchUpdateResponse(
    @SerializedName("houseId") val houseId: Int,
    @SerializedName("url") val url: String,
    @SerializedName("priceType") val priceType: String,
    @SerializedName("price") val price: Int,
    @SerializedName("priceForWs") val priceForWs: Int,
    @SerializedName("maintenanceFee") val maintenanceFee: Int,
    @SerializedName("housingType") val housingType : String,
    @SerializedName("isMultiLayer") val isMultiLayer: Boolean,
    @SerializedName("isSeparateType") val isSeparateType : Boolean,
    @SerializedName("floor") val floor: String,
    @SerializedName("size") val size: Double,
    @SerializedName("roomNum") val roomNum: Int,
    @SerializedName("washroomNum") val washroomNum: Int,
    @SerializedName("direction") val direction: String,
    @SerializedName("completionDate") val completionDate: String,
    @SerializedName("houseOption") val houseOption : String,
    @SerializedName("address") val address: String,
    @SerializedName("x") val x: Double,
    @SerializedName("y") val y: Double,
    @SerializedName("imgUrl") val imgUrl: String,
    @SerializedName("score") val score: Double
)
