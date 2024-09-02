package com.example.findhomes.data.model

data class RankingInfo(
    val rank: Int,
    val priceType: String,
    val price: Int,
    val rent: Int,
    val address: String,
    val housingType: String,
    val info: HouseInfo

)

data class HouseInfo(
    val floor: Int,
    val size: String
)
