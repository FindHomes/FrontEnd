package com.homes.findhomes.domain.repository

import com.homes.findhomes.data.model.SearchCompleteResponse
import com.homes.findhomes.data.model.WishHistoryResponse

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