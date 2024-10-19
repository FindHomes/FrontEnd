package com.example.findhomes.data.remote

import com.example.findhomes.data.di.BaseResponse
import com.example.findhomes.data.model.SearchChatResponse
import com.example.findhomes.data.model.SearchCompleteResponse
import com.example.findhomes.data.model.WishHistoryResponse
import retrofit2.http.GET
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
}