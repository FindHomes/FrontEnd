package com.example.findhomes.domain.repository

import com.example.findhomes.data.model.ManConRequest
import com.example.findhomes.data.model.SearchChatRequest
import com.example.findhomes.data.model.SearchChatResponse
import com.example.findhomes.data.model.SearchCompleteResponse
import com.example.findhomes.data.model.SearchDetailResponse

interface SearchRepository {
    suspend fun getSearchData(
        manConRequest: ManConRequest
    ) : List<SearchCompleteResponse>?

    suspend fun postChatData(
        searchChatRequest: SearchChatRequest
    ) : SearchChatResponse?

    suspend fun postManConData(
        manConRequest: ManConRequest
    ) : List<String>?

    suspend fun getSearchDetailData(
        houseId : Int
    ) : SearchDetailResponse?
}