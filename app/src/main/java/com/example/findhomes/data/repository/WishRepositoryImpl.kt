package com.example.findhomes.data.repository

import android.util.Log
import com.example.findhomes.data.model.SearchCompleteResponse
import com.example.findhomes.data.model.WishHistoryResponse
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
                Log.e("favoriteData", response.message)
                null
            }
        } catch (e: Exception) {
            Log.e("favoriteData", "wish", e)
            null
        }
    }

    override suspend fun getWishRecent(): List<SearchCompleteResponse>? {
        return try {
            val response = wishApi.wishRecent()
            if (response.success) {
                response.result
            } else {
                Log.e("recentData", response.message)
                null
            }
        } catch (e: Exception) {
            Log.e("recentData", "wish", e)
            null
        }
    }

    override suspend fun getWishHistory(): List<WishHistoryResponse>? {
        return try {
            val response = wishApi.wishHistory()
            if (response.success) {
                response.result
            } else {
                Log.e("historyData", response.message)
                null
            }
        } catch (e: Exception) {
            Log.e("historyData", "wish", e)
            null
        }
    }
}