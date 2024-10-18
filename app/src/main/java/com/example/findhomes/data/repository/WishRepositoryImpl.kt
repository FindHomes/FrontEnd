package com.example.findhomes.data.repository

import android.util.Log
import com.example.findhomes.data.model.SearchCompleteResponse
import com.example.findhomes.data.remote.WishApi
import com.example.findhomes.domain.repository.WishRepository
import javax.inject.Inject

class WishRepositoryImpl @Inject constructor(
    private val wishApi: WishApi
) : WishRepository {
    override suspend fun getWishFavorite(): List<SearchCompleteResponse>? {
        return try {
            val response = wishApi.wishFavorite()
            if (response.success) {
                response.result
            } else {
                Log.e("wishData", response.message)
                null
            }
        } catch (e: Exception) {
            Log.e("wishData", "wish", e)
            null
        }
    }
}