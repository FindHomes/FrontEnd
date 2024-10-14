package com.example.findhomes.data.remote

import com.example.findhomes.data.di.BaseResponse
import com.example.findhomes.data.model.LogInResponse
import com.example.findhomes.data.model.ManConRequest
import com.example.findhomes.data.model.SearchChatRequest
import com.example.findhomes.data.model.SearchChatResponse
import com.example.findhomes.data.model.SearchCompleteResponse
import com.example.findhomes.data.model.SearchDetailResponse
import com.example.findhomes.data.model.SearchStatisticsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchApi {

    @POST("api/search/user-chat")
    suspend fun searchChat(
        @Body request : SearchChatRequest
    ) : BaseResponse<SearchChatResponse?>

    @GET("api/search/complete")
    suspend fun searchComplete(
        @Query ("manConRequest") manConRequest: ManConRequest
    ): BaseResponse<List<SearchCompleteResponse>?>

    @POST("api/search/man-con")
    suspend fun searchManCon(
        @Body request : ManConRequest
    ) : BaseResponse<List<String>>

    @GET("api/house/{houseId}")
    suspend fun searchDetail(
        @Path("houseId") houseId : Int
    ) : BaseResponse<SearchDetailResponse?>

    @GET("api/search/statistics")
    suspend fun searchStatistics(
    ) : BaseResponse<List<SearchStatisticsResponse>?>
}