package com.example.findhomes.data

data class SearchResultData(
    val lat : Double,
    val lon : Double,
    val img : String,
    val ranking : Int,
    val score : Int,
    val priceType : String,
    val price : String,
    val room : String,
    val etc : String
)
