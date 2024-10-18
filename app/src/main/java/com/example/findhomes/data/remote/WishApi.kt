package com.example.findhomes.data.remote

import com.example.findhomes.data.di.BaseResponse
import com.example.findhomes.data.model.SearchChatResponse
import com.example.findhomes.data.model.SearchCompleteResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WishApi {
    @GET("api/houses/favorite")
    suspend fun wishFavorite(
    ) : BaseResponse<List<SearchCompleteResponse>?>
}