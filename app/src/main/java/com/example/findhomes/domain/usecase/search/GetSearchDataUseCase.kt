package com.example.findhomes.domain.usecase.search

import com.example.findhomes.data.remote.SearchCompleteResponse
import com.example.findhomes.domain.repository.SearchRepository
import javax.inject.Inject

class GetSearchDataUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke() : SearchCompleteResponse {
        return searchRepository.getSearchData()
    }
}