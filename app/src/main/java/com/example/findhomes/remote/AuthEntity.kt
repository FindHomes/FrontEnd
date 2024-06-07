package com.example.findhomes.remote

import com.example.findhomes.data.HouseInfo
import com.google.gson.annotations.SerializedName
import java.io.Serial

data class SearchResultRequest(
    @SerializedName("manCon") val manCon: EssentialData,
    @SerializedName("userInput") val userInput : String
)

data class EssentialData(
    @SerializedName("housingTypes") val housingTypes : ArrayList<String>,
    @SerializedName("prices") val prices : PricesData,
    @SerializedName("regions") val regions : ArrayList<String>
)

data class PricesData(
    @SerializedName("mm") val mm : Int,
    @SerializedName("js") val js: Int,
    @SerializedName("ws") val ws: WSData
)

data class WSData(
    @SerializedName("deposit") val deposit : Int,
    @SerializedName("rent") val rent : Int
)

data class SearchResultResponse(
    @SerializedName("rankings") val rankings : RankingData
)

data class RankingData(
    @SerializedName("rank") val rank : Int,
    @SerializedName("priceType") val priceType: Int,
    @SerializedName("price") val price : Int,
    @SerializedName("rent") val rent : Int,
    @SerializedName("address") val address : Int,
    @SerializedName("housingType") val housingType: Int,
    @SerializedName("info") val rankInfo: RankInfo,
)

data class RankInfo(
    @SerializedName("floor") val float: Int,
    @SerializedName("size") val size : String
)