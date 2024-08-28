package com.example.findhomes.remote

import android.util.Log
import com.example.findhomes.ApplicationClass.Companion.X_ACCESS_TOKEN
import com.example.findhomes.local.getJwt
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class XAccessTokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        val jwtToken : String? = getJwt()

        jwtToken?.let{
            builder.addHeader(X_ACCESS_TOKEN, "Bearer $jwtToken")
        }
        Log.d("XAccessTokenInterceptor", builder.build().header(X_ACCESS_TOKEN).toString())

        return chain.proceed(builder.build())
    }
}