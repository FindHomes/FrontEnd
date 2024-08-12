package com.example.findhomes

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.findhomes.remote.XAccessTokenInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApplicationClass : Application() {

    companion object{
        const val X_ACCESS_TOKEN : String = "Authorization"
        const val DEV_URL : String = "http://3.39.75.112:8080"
        const val PROD_URL : String = "" // 배포 url

        const val BASE_URL : String = DEV_URL

        lateinit var retrofit: Retrofit
        lateinit var mSharedPreferences: SharedPreferences
    }

    val client : OkHttpClient = OkHttpClient.Builder()
        .readTimeout(30000, TimeUnit.MILLISECONDS)
        .connectTimeout(30000, TimeUnit.MILLISECONDS)
        .addNetworkInterceptor(XAccessTokenInterceptor())
        .build()

    override fun onCreate() {
        super.onCreate()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        mSharedPreferences = applicationContext.getSharedPreferences("My App Spf", Context.MODE_PRIVATE)
    }
}