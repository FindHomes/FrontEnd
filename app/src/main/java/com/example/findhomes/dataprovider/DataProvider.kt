package com.example.findhomes.dataprovider

import com.example.findhomes.data.City
import com.example.findhomes.data.County

object DataProvider {
    val cities = listOf(
        //서울
        City("서울", listOf(
            County("전체"), County("강남구"), County("강동구"), County("강북구"),
            County("강서구"), County("관악구") ,County("광진구"), County("구로구"),
            County("금천구"), County("노원구"), County("도봉구"), County("동대문구"),
            County("동작구"), County("마포구"), County("서대문구"), County("서초구"),
            County("서초구"), County("성북구"), County("송파구"), County("양천구"),
            County("영등포구"), County("용산구"), County("은평구"), County("종로구"),
            County("중구"), County("중랑구"))),

        //경기
        City("경기", listOf(
            County("전체"), County("가평군"), County("고양시 덕양구"), County("고양시 일산동구"),
            County("성북구"), County("성북구"), County("성북구"), County("성북구"))),

        //인천
        City("인천", listOf(
            County("전체"), County("가평군"), County("고양시 덕양구"), County("고양시 일산동구"),
            County("성북구"), County("성북구"), County("성북구"), County("성북구"))),

        //부산
        City("부산", listOf(
            County("전체"), County("가평군"), County("고양시 덕양구"), County("고양시 일산동구"),
            County("성북구"), County("성북구"), County("성북구"), County("성북구"))),

        //대구
        City("대구", listOf(
            County("전체"), County("가평군"), County("고양시 덕양구"), County("고양시 일산동구"),
            County("성북구"), County("성북구"), County("성북구"), County("성북구"))),

        //광주
        City("광주", listOf(
            County("전체"), County("가평군"), County("고양시 덕양구"), County("고양시 일산동구"),
            County("성북구"), County("성북구"), County("성북구"), County("성북구"))),

        //대전
        City("대전", listOf(
            County("전체"), County("가평군"), County("고양시 덕양구"), County("고양시 일산동구"),
            County("성북구"), County("성북구"), County("성북구"), County("성북구"))),

        //울산
        City("울산", listOf(
            County("전체"), County("가평군"), County("고양시 덕양구"), County("고양시 일산동구"),
            County("성북구"), County("성북구"), County("성북구"), County("성북구"))),

        //경남
        City("경남", listOf(
            County("전체"), County("가평군"), County("고양시 덕양구"), County("고양시 일산동구"),
            County("성북구"), County("성북구"), County("성북구"), County("성북구"))),

        //경북
        City("경북", listOf(
            County("전체"), County("가평군"), County("고양시 덕양구"), County("고양시 일산동구"),
            County("성북구"), County("성북구"), County("성북구"), County("성북구"))),

        //충남
        City("충남", listOf(
            County("전체"), County("가평군"), County("고양시 덕양구"), County("고양시 일산동구"),
            County("성북구"), County("성북구"), County("성북구"), County("성북구"))),

        //충북
        City("충북", listOf(
            County("전체"), County("가평군"), County("고양시 덕양구"), County("고양시 일산동구"),
            County("성북구"), County("성북구"), County("성북구"), County("성북구"))),

        //전남
        City("전남", listOf(
            County("전체"), County("가평군"), County("고양시 덕양구"), County("고양시 일산동구"),
            County("성북구"), County("성북구"), County("성북구"), County("성북구"))),

        //전북
        City("전북", listOf(
            County("전체"), County("가평군"), County("고양시 덕양구"), County("고양시 일산동구"),
            County("성북구"), County("성북구"), County("성북구"), County("성북구"))),

        //강원
        City("강원", listOf(
            County("전체"), County("가평군"), County("고양시 덕양구"), County("고양시 일산동구"),
            County("성북구"), County("성북구"), County("성북구"), County("성북구"))),

        //제주
        City("제주", listOf(
            County("전체"), County("가평군"), County("고양시 덕양구"), County("고양시 일산동구"),
            County("성북구"), County("성북구"), County("성북구"), County("성북구"))),

        //세종
        City("세종", listOf(
            County("전체"), County("가평군"), County("고양시 덕양구"), County("고양시 일산동구"),
            County("성북구"), County("성북구"), County("성북구"), County("성북구"))),
    )
}