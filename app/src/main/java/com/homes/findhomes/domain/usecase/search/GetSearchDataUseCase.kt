package com.homes.findhomes.domain.usecase.search

import com.homes.findhomes.data.model.ManConRequest
import com.homes.findhomes.data.model.SearchCompleteResponse
import com.homes.findhomes.domain.repository.SearchRepository
import javax.inject.Inject

class GetSearchDataUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(manConRequest: ManConRequest) : List<SearchCompleteResponse>? {
        return searchRepository.getSearchData(manConRequest)
    }
}