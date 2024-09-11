package com.example.findhomes.data.remote

import com.example.findhomes.data.di.BaseResponse
import com.example.findhomes.data.model.ManConRequest
import com.example.findhomes.data.model.SearchChatRequest
import com.example.findhomes.data.model.SearchCompleteResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SearchApi {

    @POST("api/search/user-chat")
    suspend fun searchChat(
        @Body request : SearchChatRequest
    ) : BaseResponse<String>

    @GET("test/api/search/complete")
    suspend fun searchComplete(
    ): BaseResponse<SearchCompleteResponse?>

    @POST("api/search/man-con")
    suspend fun searchManCon(
        @Body request : ManConRequest
    ) : BaseResponse<List<String>>
}