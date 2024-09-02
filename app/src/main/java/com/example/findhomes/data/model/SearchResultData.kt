package com.example.findhomes.data.model

data class SearchResultData(
    val houseId : Int,
    val img : String,
    val priceType : String,
    val price : String,
    val lat : Double,
    val lon : Double,
    val score : Int,
    val room : String,
    val washRoom : String,
    val size : Double
)
