package com.homes.findhomes.data.remote

import com.homes.findhomes.data.di.BaseResponse
import com.homes.findhomes.data.model.LogInResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface LogInApi {
    @GET("api/oauth/kakao")
    suspend fun logIn(
        @Query("accessToken") accessToken : String
    ) : BaseResponse<LogInResponse?>
}