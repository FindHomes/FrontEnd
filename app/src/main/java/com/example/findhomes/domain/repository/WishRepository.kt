package com.example.findhomes.domain.repository

import com.example.findhomes.data.model.SearchCompleteResponse

interface WishRepository {
    suspend fun getWishFavorite(
    ): List<SearchCompleteResponse>?
}