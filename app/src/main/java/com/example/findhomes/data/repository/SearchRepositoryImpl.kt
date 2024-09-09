package com.example.findhomes.data.repository

import android.util.Log
import com.example.findhomes.data.model.SearchCompleteResponse
import com.example.findhomes.data.remote.SearchApi
import com.example.findhomes.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchApi: SearchApi
) : SearchRepository {
    override suspend fun getSearchData(): SearchCompleteResponse? {
        return try {
            val response = searchApi.searchComplete()
            if (response.success) {
                response.result
            } else {
                // 로그 기록, 오류 메시지 처리 등
                null
            }
        } catch (e: Exception) {
            // 예외 처리 로그 기록 등
            null
        }
    }

    override suspend fun postChatData(userInput : String) : String? {
        return try {
            val response = searchApi.searchChat(userInput)
            if (response.success) {
                response.result
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}