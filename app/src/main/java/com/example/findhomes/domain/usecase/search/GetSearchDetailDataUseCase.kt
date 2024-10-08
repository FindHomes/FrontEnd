package com.example.findhomes.domain.usecase.search

import com.example.findhomes.data.model.SearchDetailResponse
import com.example.findhomes.domain.repository.SearchRepository
import javax.inject.Inject

class GetSearchDetailDataUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(houseId : Int) : SearchDetailResponse? {
        return searchRepository.getSearchDetailData(houseId)
    }
}