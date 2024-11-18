package com.homes.findhomes.domain.usecase.search

import com.homes.findhomes.data.model.SearchDetailResponse
import com.homes.findhomes.data.model.SearchRecommendResponse
import com.homes.findhomes.domain.repository.SearchRepository
import javax.inject.Inject

class GetSearchDetailDataUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(houseId : Int) : SearchRecommendResponse? {
        return searchRepository.getSearchDetailData(houseId)
    }
}