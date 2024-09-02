package com.example.findhomes

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.findhomes.data.di.XAccessTokenInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApplicationClass : Application() {
}