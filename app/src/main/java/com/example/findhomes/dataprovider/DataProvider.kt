package com.example.findhomes.dataprovider

import com.example.findhomes.data.City
import com.example.findhomes.data.County

object DataProvider {
    val cities = listOf(
        City("서울", listOf(
            County("전체"), County("강남구"), County("강동구"), County("강북구"),
            County("강서구"), County("관악구") ,County("광진구"), County("구로구"),
            County("금천구"), County("노원구"), County("도봉구"), County("동대문구"),
            County("동작구"), County("마포구"), County("서대문구"), County("서초구"),
            County("서초구"), County("성북구"))),
        City("경기", listOf(County("Yeongtong-gu"), County("Paldal-gu")))

    )
}