package com.example.findhomes.domain.repository

import com.example.findhomes.data.remote.SearchCompleteResponse

interface SearchRepository {
    suspend fun getSearchData(
    ) : SearchCompleteResponse
}