package com.example.findhomes.domain.repository

import com.example.findhomes.data.model.SearchCompleteResponse

interface SearchRepository {
    suspend fun getSearchData(
    ) : SearchCompleteResponse?

    suspend fun postChatData(
        userInput : String
    ) : String?
}