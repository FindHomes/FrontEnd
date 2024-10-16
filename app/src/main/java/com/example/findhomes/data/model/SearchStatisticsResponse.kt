package com.example.findhomes.data.model

data class SearchStatisticsResponse(
    val keyword: String,
    val houseConditions: List<String>,
    val houseOptions: List<String>,
    val facilityAndInfos: List<GraphDataResponse>,
    val publicDataAndInfos: List<GraphDataResponse>
)

data class GraphDataResponse(
    val dataName: String,
    val houseAndValues: List<HouseValueResponse>
)

data class HouseValueResponse(
    val houseId: Int,
    val ranking: Int,
    val value: List<StatisticsValue>
)

data class StatisticsValue(
    val name : String,
    val value : Double
)
