package com.example.findhomes

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.findhomes.data.di.XAccessTokenInterceptor
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class ApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)

    }
}