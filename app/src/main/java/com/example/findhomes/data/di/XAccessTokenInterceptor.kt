package com.example.findhomes.data.di

import android.content.Context
import android.util.Log
import com.example.findhomes.data.getAccessToken
import okhttp3.Interceptor
import okhttp3.Response

class XAccessTokenInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()

        builder.addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhMjgwMmI0MS04YjAwLTQzZGYtYWQ3My1lYmMwZWQ0NjNkNzIiLCJpYXQiOjE3MjYwNDM2NTgsImV4cCI6MTcyNjA0NzI1OH0.tVZyMNwPHgYTVZ0tST5Rk5Li1wl4MJvZErPWdB9DO8Y")

        // 카카오 로그인 연동 시 구현
//        val accessToken = getAccessToken(context)
//
//        accessToken?.let {
//            builder.addHeader("Authorization", it)
//            Log.d("NetworkInterceptor", "Authorization: $it")
//        }

        return chain.proceed(builder.build())
    }

    companion object {
        const val AUTHORIZATION = "Authorization"
    }
}