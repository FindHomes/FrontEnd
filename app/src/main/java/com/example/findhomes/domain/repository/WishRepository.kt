package com.example.findhomes.domain.repository

import com.example.findhomes.data.model.SearchCompleteResponse
import com.example.findhomes.data.model.WishHistoryResponse

interface WishRepository {
    suspend fun getWishFavorite(
    ): List<SearchCompleteResponse>?

    suspend fun getWishRecent(
    ): List<SearchCompleteResponse>?

    suspend fun getWishHistory(
    ): List<WishHistoryResponse>?

    suspend fun deleteWishHistory(
        searchLogId : Int
    ) : String?
}