package com.example.findhomes.data.remote

import com.example.findhomes.data.di.BaseResponse
import com.example.findhomes.data.model.SearchCompleteResponse
import com.example.findhomes.data.model.SearchUpdateResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SearchApi {

    @POST("api/search/user-chat")
    fun searchChat(
        @Body request : String
    ) : BaseResponse<String>

    @GET("test/api/search/complete")
    suspend fun searchComplete(
    ): BaseResponse<SearchCompleteResponse?>

    @GET("test/api/search/update")
    fun searchUpdate(
        @Query("xMax") xMax : Double,
        @Query("xMin") xMin : Double,
        @Query("yMax") yMax : Double,
        @Query("yMin") yMin : Double)
            : BaseResponse<SearchUpdateResponse>

//    @POST("api/search/man-con")
//    fun searchEssential(
//        @Body request : SearchEssentialRequest
//    ) : SearchEssentialResponse
}