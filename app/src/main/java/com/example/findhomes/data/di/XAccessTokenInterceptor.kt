package com.example.findhomes.data.di

import android.content.Context
import android.util.Log
import com.example.findhomes.data.getAccessToken
import okhttp3.Interceptor
import okhttp3.Response

class XAccessTokenInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
//        val accessToken = getAccessToken(context)

//        accessToken?.let {
            builder.addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhMjgwMmI0MS04YjAwLTQzZGYtYWQ3My1lYmMwZWQ0NjNkNzIiLCJpYXQiOjE3MjUzNDM1NjksImV4cCI6MTcyNTM0NzE2OX0.MzTqJlGZ510E5jkkAio5U5aW1NnQ3j7O40b13zFx8QU")
//            Log.d("NetworkInterceptor", "Authorization: $it")
//        }

        return chain.proceed(builder.build())
    }

    companion object {
        const val AUTHORIZATION = "Authorization"
    }
}