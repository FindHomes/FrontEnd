package com.example.findhomes.data.dataprovider

import com.example.findhomes.data.model.City
import com.example.findhomes.data.model.County
import com.example.findhomes.data.model.SeekBarData

object DataProvider {
    val cities = listOf(
        //서울
        City("서울", listOf(
            County("전체"), County("강남구"), County("강동구"), County("강북구"),
            County("강서구"), County("관악구") , County("광진구"), County("구로구"),
            County("금천구"), County("노원구"), County("도봉구"), County("동대문구"),
            County("동작구"), County("마포구"), County("서대문구"), County("서초구"),
            County("서초구"), County("성북구"), County("송파구"), County("양천구"),
            County("영등포구"), County("용산구"), County("은평구"), County("종로구"),
            County("중구"), County("중랑구")
        )),

        //경기
        City("경기", listOf(
            County("전체"), County("가평군"), County("고양시 덕양구"), County("고양시 일산동구"),
            County("고양시 일산서구"), County("과천시"), County("광명시"), County("광주시"),
            County("광주시"), County("구리시"), County("군포시"), County("김포시"),
            County("남양주시"), County("동두천시"), County("부천시"), County("성남시 분당구"),
            County("성남시 수정구"), County("성남시 중원구"), County("수원시 권선구"), County("수원시 영통구"),
            County("수원시 장안구"), County("수원시 팔달구"), County("시흥시"), County("안산시 단원구"),
            County("안산시 상록구"), County("안성시"), County("안양시 동안구"), County("안양시 만안구"),
            County("양주시"), County("양평군"), County("여주시"), County("연천군"),
            County("오산시"), County("용인시 기흥구"), County("용인시 수지구"), County("용인시 처인구"),
            County("의왕시"), County("의정부시"), County("이천시"), County("파주시"),
            County("평택시"), County("포천시"), County("하남시"), County("화성시")
        )),

        //인천
        City("인천", listOf(
            County("전체"), County("강화군"), County("계양구"), County("미추홀구"),
            County("남동구"), County("동구"), County("부평구"), County("서구"),
            County("연수구"), County("옹진군"), County("중구")
        )),

        //부산
        City("부산", listOf(
            County("전체"), County("강서구"), County("금정구"), County("기장군"),
            County("남구"), County("동구"), County("동래구"), County("부산진구"),
            County("북구"), County("사상구"), County("사하구"), County("서구"),
            County("수영구"), County("연제구"), County("영도구"), County("중구"),
            County("해운대구")
        )),

        //대구
        City("대구", listOf(
            County("전체"), County("군위군"), County("남구"), County("달서구"),
            County("달성군"), County("동구"), County("북구"), County("서구"),
            County("수성구"), County("중구")
        )),

        //광주
        City("광주", listOf(
            County("전체"), County("광산구"), County("남구"), County("동구"),
            County("북구"), County("서구")
        )),

        //대전
        City("대전", listOf(
            County("전체"), County("대덕구"), County("동구"), County("서구"),
            County("유성구"), County("중구")
        )),

        //울산
        City("울산", listOf(
            County("전체"), County("남구"), County("동구"), County("북구"),
            County("울주군"), County("중구")
        )),

        //경남
        City("경남", listOf(
            County("전체"), County("가평군"), County("고양시 덕양구"), County("고양시 일산동구"),
            County("성북구"), County("성북구"), County("성북구"), County("성북구")
        )),

        //경북
        City("경북", listOf(
            County("전체"), County("경산시"), County("경주시"), County("고령군"),
            County("구미시"), County("김천시"), County("문경시"), County("봉화군"),
            County("상주시"), County("성주군"), County("안동시"), County("영덕군"),
            County("영양군"), County("영주시"), County("영천시"), County("예천군"),
            County("울릉군"), County("울진군"), County("의성군"), County("청도군"),
            County("청송군"), County("칠곡군"), County("포항시 남구"), County("포항시 북구")
        )),

        //충남
        City("충남", listOf(
            County("전체"), County("가평군"), County("고양시 덕양구"), County("고양시 일산동구"),
            County("성북구"), County("성북구"), County("성북구"), County("성북구")
        )),

        //충북
        City("충북", listOf(
            County("전체"), County("가평군"), County("고양시 덕양구"), County("고양시 일산동구"),
            County("성북구"), County("성북구"), County("성북구"), County("성북구")
        )),

        //전남
        City("전남", listOf(
            County("전체"), County("가평군"), County("고양시 덕양구"), County("고양시 일산동구"),
            County("성북구"), County("성북구"), County("성북구"), County("성북구")
        )),

        //전북
        City("전북", listOf(
            County("전체"), County("가평군"), County("고양시 덕양구"), County("고양시 일산동구"),
            County("성북구"), County("성북구"), County("성북구"), County("성북구")
        )),

        //강원
        City("강원", listOf(
            County("전체"), County("가평군"), County("고양시 덕양구"), County("고양시 일산동구"),
            County("성북구"), County("성북구"), County("성북구"), County("성북구")
        )),

        //제주
        City("제주", listOf(
            County("전체"), County("가평군"), County("고양시 덕양구"), County("고양시 일산동구"),
            County("성북구"), County("성북구"), County("성북구"), County("성북구")
        )),

        //세종
        City("세종", listOf(
            County("전체"), County("가평군"), County("고양시 덕양구"), County("고양시 일산동구"),
            County("성북구"), County("성북구"), County("성북구"), County("성북구")
        )),
    )

    private val sbData = listOf(
        SeekBarData("월세", listOf("~0만원","~5만원","~10만원","~15만원","~20만원","~25만원","~30만원",
            "~35만원","~40만원","~45만원","~50만원","~51만원","~52만원","~53만원","~54만원","~55만원","~56만원",
            "~57만원","~58만원","~59만원","~60만원","~50만원","~50만원","~50만원", "무제한")),
        SeekBarData("보증금", listOf(
            "~0원","~100만원","~200만원","~300만원","~400만원","~500만원",
            "~1000만원","~1500만원","~2000만원","~2500만원","~3000만원","~3500만원","~4000만원","~4500만원",
            "~5000만원","~5500만원","~6000만원","~6500만원",
            "~7000만원","~8000만원","~9000만원","~1억원","~1억 1000만원","~1억 2000만원","~1억 3000만원",
            "~1억 4000만원","~1억 5000만원","~1억 6000만원","~1억 7000만원","~1억 8000만원","~1억 9000만원","~2억",
            "~2억 5000만원", "~3억", "~3억 5000만원", "4억", "~3억 5000만원", "5억", "~5억 5000만원",
            "~6억","~6억 5000만원","7억","~7억 5000만원","8억","~8억 5000만원","9억","무제한")),
        SeekBarData("매매", listOf(
            "~0원","~100만원","~200만원","~300만원","~400만원","~500만원",
            "~1000만원","~1500만원","~2000만원","~2500만원","~3000만원","~3500만원","~4000만원","~4500만원",
            "~5000만원","~5500만원","~6000만원","~6500만원",
            "~7000만원","~8000만원","~9000만원","~1억원","~1억 1000만원","~1억 2000만원","~1억 3000만원",
            "~1억 4000만원","~1억 5000만원","~1억 6000만원","~1억 7000만원","~1억 8000만원","~1억 9000만원","~2억",
            "2.5", "3", "3.5", "4", "4.5", "5", "5.5",
            "6","6.5","7","7.5","8","8.5","9","무제한"))
    )

    fun getSbData(type: String, progress: Int): String {
        return sbData.firstOrNull { it.type == type }?.progress?.getOrNull(progress) ?: "N/A"
    }
}