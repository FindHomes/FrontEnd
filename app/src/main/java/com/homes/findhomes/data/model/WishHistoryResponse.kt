package com.homes.findhomes.data.model

data class WishHistoryResponse (
    val searchLogId : Int,
    val date : String,
    val keyword : String,
    val regionInfo : String,
    val typeInfo : String,
    val priceInfo : String
)