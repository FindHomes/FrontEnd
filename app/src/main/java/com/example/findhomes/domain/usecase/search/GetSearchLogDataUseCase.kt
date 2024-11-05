package com.example.findhomes.domain.usecase.search

import com.example.findhomes.data.model.SearchCompleteResponse
import com.example.findhomes.domain.repository.SearchRepository
import javax.inject.Inject

class GetSearchLogDataUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(searchLogId: Int) : List<SearchCompleteResponse>? {
        return searchRepository.getSearchLogData(searchLogId)
    }
}