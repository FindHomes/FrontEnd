package com.example.findhomes.data.remote

import com.example.findhomes.data.di.BaseResponse
import com.example.findhomes.data.model.LogInResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface LogInApi {
    @GET("api/oauth/kakao")
    suspend fun logIn(
        @Query("accessToken") accessToken : String
    ) : BaseResponse<LogInResponse?>
}