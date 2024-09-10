package com.example.findhomes.domain.repository

import com.example.findhomes.data.model.ManConRequest
import com.example.findhomes.data.model.SearchCompleteResponse

interface SearchRepository {
    suspend fun getSearchData(
    ) : SearchCompleteResponse?

    suspend fun postChatData(
        userInput : String
    ) : String?

    suspend fun postManConData(
        manConRequest: ManConRequest
    ) : List<String>?
}