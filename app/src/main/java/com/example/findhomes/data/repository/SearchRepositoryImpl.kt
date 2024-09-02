package com.example.findhomes.data.repository

import com.example.findhomes.data.remote.SearchApi
import com.example.findhomes.data.remote.SearchCompleteResponse
import com.example.findhomes.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchApi: SearchApi
) : SearchRepository {
    override suspend fun getSearchData(): SearchCompleteResponse {
        return searchApi.searchComplete()
    }
}