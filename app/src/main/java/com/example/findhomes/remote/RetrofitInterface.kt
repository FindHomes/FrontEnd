package com.example.findhomes.remote

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface RetrofitInterface {

    @POST("api/search")
    fun searchResultInfo(
        @Body request: SearchResultRequest
    ): Call<BaseResponse<ArrayList<SearchResultResponse>>>
}