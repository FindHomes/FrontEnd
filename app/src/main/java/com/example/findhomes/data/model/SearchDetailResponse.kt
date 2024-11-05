package com.example.findhomes.data.model

data class SearchDetailResponse(
    val ranking : Int,
    val houseId: Int,
    val url: String,
    val priceType: String,
    val price: Int,
    val priceForWs: Int,
    val maintenanceFee: Int,
    val housingType : String,
    val isMultiLayer: Boolean,
    val isSeparateType : Boolean,
    val floor: String,
    val size: Double,
    val roomNum: Int,
    val washroomNum: Int,
    val direction: String,
    val completionDate: String,
    val houseOption : String,
    val address: String,
    val x: Double,
    val y: Double,
    val imgUrl: List<String>,
    val updateDate : String,
    val score: Double,
    val favorite : Boolean
)