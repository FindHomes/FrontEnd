package com.example.findhomes.data.repository

import android.util.Log
import com.example.findhomes.data.model.ManConRequest
import com.example.findhomes.data.model.SearchChatRequest
import com.example.findhomes.data.model.SearchChatResponse
import com.example.findhomes.data.model.SearchCompleteResponse
import com.example.findhomes.data.model.SearchDetailResponse
import com.example.findhomes.data.model.SearchStatisticsResponse
import com.example.findhomes.data.remote.SearchApi
import com.example.findhomes.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchApi: SearchApi
) : SearchRepository {
    override suspend fun getSearchData(manConRequest: ManConRequest): List<SearchCompleteResponse>? {
        return try {
            val response = searchApi.searchComplete(manConRequest)
            if (response.success) {
                response.result
            } else {
                Log.e("getSearchData", response.message)
                null
            }
        } catch (e: Exception) {
            Log.e("getSearchData", "search", e)
            null
        }
    }

    override suspend fun getSearchLogData(searchLogId: Int): List<SearchCompleteResponse>? {
        return try {
            val response = searchApi.searchLogId(searchLogId)
            if (response.success) {
                response.result
            } else {
                Log.e("getSearchLogData", response.message)
                null
            }
        } catch (e: Exception) {
            Log.e("getSearchLogData", "search", e)
            null
        }
    }

    override suspend fun postChatData(searchChatRequest: SearchChatRequest) : SearchChatResponse? {
        return try {
            val response = searchApi.searchChat(searchChatRequest)
            if (response.success) {
                response.result
            } else {
                Log.d("postChatData", response.message)
                null
            }
        } catch (e: Exception) {
            Log.e("postChatData","chat",e)
            null
        }
    }

    override suspend fun postManConData(manConRequest: ManConRequest): List<String>? {
        return try {
            val response = searchApi.searchManCon(manConRequest)
            if (response.success) {
                response.result
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getSearchDetailData(houseId: Int): SearchDetailResponse? {
        return try {
            val response = searchApi.searchDetail(houseId)
            if (response.success) {
                response.result
            } else {
                Log.e("getSearchDetailData", response.message)
                null
            }
        } catch (e: Exception) {
            Log.e("getSearchDetailData", "detail", e)
            null
        }
    }

    override suspend fun postSearchFavoriteData(
        houseId: Int,
        action: String
    ): SearchDetailResponse? {
        return try {
            val response = searchApi.searchFavorite(houseId, action)
            if(response.success){
                response.result
            } else {
                Log.e("postSearchFavoriteData", response.message)
                null
            }
        } catch (e: Exception){
            Log.e("postSearchFavoriteData", "favorite", e)
            null
        }
    }

    override suspend fun getStatisticsData(): List<SearchStatisticsResponse>? {
        return try {
            val response = searchApi.searchStatistics()
            if(response.success){
                response.result
            } else {
                Log.e("getStatisticsData", response.message)
                null
            }
        } catch (e: Exception){
            Log.e("getStatisticsData", "statistics", e)
            null
        }
    }

    override suspend fun postSearchLogsData(): String?{
        return try {
            val response = searchApi.searchLogs()
            if(response.success){
                response.result
            } else {
                Log.e("postSearchLogs", response.message)
                null
            }
        } catch (e: Exception){
            Log.e("postSearchLogs", "statistics", e)
            null
        }
    }
}