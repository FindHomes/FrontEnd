package com.example.findhomes.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RetrofitInterface {

    // SearchComplete API
//    @GET("test/api/search/complete")
//    fun searchComplete()
//    : Call<BaseResponse<SearchCompleteResponse>>

    // SearchComplete API
    @GET("test/api/search/complete")
    fun searchComplete()
    : Call<SearchCompleteResponse>

    // SearchUpdate API
    @GET("test/api/search/update")
    fun searchUpdate(
        @Query ("xMax") xMax : Double,
        @Query ("xMin") xMin : Double,
        @Query ("yMax") yMax : Double,
        @Query ("yMin") yMin : Double)
    : Call<BaseResponse<SearchUpdateResponse>>
}