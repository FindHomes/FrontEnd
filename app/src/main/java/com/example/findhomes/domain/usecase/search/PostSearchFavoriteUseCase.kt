package com.example.findhomes.domain.usecase.search

import com.example.findhomes.data.model.SearchDetailResponse
import com.example.findhomes.domain.repository.SearchRepository
import javax.inject.Inject

class PostSearchFavoriteUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(houseId: Int, action: String) : SearchDetailResponse? {
        return searchRepository.postSearchFavoriteData(houseId, action)
    }
}