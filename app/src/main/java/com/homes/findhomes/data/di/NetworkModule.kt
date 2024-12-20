package com.homes.findhomes.data.di

import android.content.Context
import com.homes.findhomes.data.remote.LogInApi
import com.homes.findhomes.data.remote.SearchApi
import com.homes.findhomes.data.remote.WishApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return logging
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor, @ApplicationContext context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(50000, TimeUnit.MILLISECONDS)
            .connectTimeout(50000, TimeUnit.MILLISECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(XAccessTokenInterceptor(context))
            .authenticator(TokenAuthenticator(context))  // Authenticator 추가
            // EOFException 임의 처리
            .retryOnConnectionFailure(true)
            .cookieJar(MyCookieJar())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://3.39.75.112:8080/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideSearchApi(retrofit: Retrofit): SearchApi {
        return retrofit.create(SearchApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLogInApi(retrofit: Retrofit) : LogInApi{
        return retrofit.create(LogInApi::class.java)
    }

    @Provides
    @Singleton
    fun provideWishApi(retrofit: Retrofit) : WishApi{
        return retrofit.create(WishApi::class.java)
    }

}
