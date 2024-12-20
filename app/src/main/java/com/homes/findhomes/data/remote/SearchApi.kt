package com.homes.findhomes.data.remote

import com.homes.findhomes.data.di.BaseResponse
import com.homes.findhomes.data.model.LogInResponse
import com.homes.findhomes.data.model.ManConRequest
import com.homes.findhomes.data.model.SearchChatRequest
import com.homes.findhomes.data.model.SearchChatResponse
import com.homes.findhomes.data.model.SearchCompleteResponse
import com.homes.findhomes.data.model.SearchDetailResponse
import com.homes.findhomes.data.model.SearchRecommendResponse
import com.homes.findhomes.data.model.SearchStatisticsResponse
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

    @GET("api/search-logs/{searchLogId}/complete")
    suspend fun searchLogId(
        @Path("searchLogId") searchLogId : Int
    ) : BaseResponse<List<SearchCompleteResponse>?>

    @POST("api/search/man-con")
    suspend fun searchManCon(
        @Body request : ManConRequest
    ) : BaseResponse<List<String>>

    @GET("api/search/houses/{houseId}")
    suspend fun searchDetail(
        @Path("houseId") houseId : Int
    ) : BaseResponse<SearchRecommendResponse?>

    @GET("api/houses/{houseId}")
    suspend fun searchFavoriteDetail(
        @Path("houseId") houseId : Int
    ) : BaseResponse<SearchDetailResponse?>

    @POST("api/houses/{houseId}/favorite")
    suspend fun searchFavorite(
        @Path("houseId") houseId: Int,
        @Query("action") action: String
    ) : BaseResponse<SearchDetailResponse?>

    @GET("api/search/statistics")
    suspend fun searchStatistics(
    ) : BaseResponse<List<SearchStatisticsResponse>?>

    @POST("api/search-logs")
    suspend fun searchLogs(
    ) : BaseResponse<String?>
}