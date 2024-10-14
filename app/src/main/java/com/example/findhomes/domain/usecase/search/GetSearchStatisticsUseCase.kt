package com.example.findhomes.domain.usecase.search

import com.example.findhomes.data.model.ManConRequest
import com.example.findhomes.data.model.SearchCompleteResponse
import com.example.findhomes.data.model.SearchStatisticsResponse
import com.example.findhomes.domain.repository.SearchRepository
import javax.inject.Inject

class GetSearchStatisticsUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke() : List<SearchStatisticsResponse>? {
        return searchRepository.getStatisticsData()
    }
}