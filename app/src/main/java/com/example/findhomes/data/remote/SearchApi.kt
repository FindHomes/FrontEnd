package com.example.findhomes.data.remote

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SearchApi {

    @GET("test/api/search/complete")
    suspend fun searchComplete(
    ): SearchCompleteResponse

    @GET("test/api/search/update")
    fun searchUpdate(
        @Query("xMax") xMax : Double,
        @Query("xMin") xMin : Double,
        @Query("yMax") yMax : Double,
        @Query("yMin") yMin : Double)
            : SearchUpdateResponse

    @POST("api/search/man-con")
    fun searchEssential(
        @Body request : SearchEssentialRequest
    ) : SearchEssentialResponse

    // SearchChat API
    @POST("api/search/user-chat")
    fun searchChat(
        @Body request : SearchChatRequest
    ) : SearchChatResponse
}