package com.example.findhomes.data.di

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class MyCookieJar : CookieJar {
    private val cookieStore = HashMap<String, List<Cookie>>()

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        // 쿠키를 저장, URL의 호스트 이름을 키로 사용
        cookieStore[url.host] = cookies
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        // 요청에 포함할 쿠키를 로드
        return cookieStore[url.host] ?: ArrayList()
    }
}
