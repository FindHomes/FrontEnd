package com.example.findhomes.data.model

data class SearchCompleteResponse(
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
    var favorite: Boolean,
    val score: Double
)