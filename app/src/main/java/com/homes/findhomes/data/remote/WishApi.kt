package com.homes.findhomes.data.remote

import com.homes.findhomes.data.di.BaseResponse
import com.homes.findhomes.data.model.SearchChatResponse
import com.homes.findhomes.data.model.SearchCompleteResponse
import com.homes.findhomes.data.model.WishHistoryResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WishApi {
    @GET("api/houses/favorite")
    suspend fun wishFavorite(
    ) : BaseResponse<List<SearchCompleteResponse>?>

    @GET("api/houses/recently-viewed")
    suspend fun wishRecent(
    ) : BaseResponse<List<SearchCompleteResponse>?>

    @GET("api/search-logs")
    suspend fun wishHistory(
    ) : BaseResponse<List<WishHistoryResponse>?>

    @DELETE("api/search-logs/{searchLogId}")
    suspend fun deleteHistory(
        @Path("searchLogId") searchLogId: Int
    ) : BaseResponse<String?>
}