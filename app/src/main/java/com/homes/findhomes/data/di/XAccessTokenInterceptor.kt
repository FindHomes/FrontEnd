package com.homes.findhomes.data.di

import android.content.Context
import android.util.Log
import com.homes.findhomes.data.getAccessToken
import okhttp3.Interceptor
import okhttp3.Response

class XAccessTokenInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()

        // 카카오 로그인 연동 시 구현
        val accessToken = getAccessToken(context)

        accessToken?.let {
            builder.addHeader("Authorization", it)
            Log.d("XAccessTokenInterceptor", "요청에 사용된 토큰: $it")
        }

        return chain.proceed(builder.build())
    }

    companion object {
        const val AUTHORIZATION = "Authorization"
    }
}