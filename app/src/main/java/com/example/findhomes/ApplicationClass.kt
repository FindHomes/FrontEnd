package com.example.findhomes

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.findhomes.remote.XAccessTokenInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApplicationClass: Application() {

    companion object{
        const val X_ACCESS_TOKEN : String = "Authorization"

        const val DEV_URL : String = "http://54.180.162.207:8080/"
        const val PROD_URL : String = "http://kuit_prod_url/"

        const val BASE_URL : String = DEV_URL

        lateinit var retrofit : Retrofit
        lateinit var mSharedPreferencesManager: SharedPreferences//SharedPreferences받음 즉 전체에서 이거 사용 가능


    }
    val client : OkHttpClient = OkHttpClient.Builder()
        .readTimeout(30000, TimeUnit.MILLISECONDS)
        .connectTimeout(30000, TimeUnit.MILLISECONDS)
        .addNetworkInterceptor(XAccessTokenInterceptor())
        .build()
    //client : header를 넣을 때 client를 활용하여 넣음

    override fun onCreate() {//제일 먼저 시작
        super.onCreate()//레트로핏 만듬
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        mSharedPreferencesManager=applicationContext.getSharedPreferences("My App spf", Context.MODE_PRIVATE)
    }


}