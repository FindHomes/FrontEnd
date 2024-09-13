package com.example.findhomes.domain.usecase.search

import com.example.findhomes.data.model.ManConRequest
import com.example.findhomes.domain.repository.SearchRepository
import javax.inject.Inject

class PostManConUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(manConRequest: ManConRequest) : List<String>? {
        return searchRepository.postManConData(manConRequest)
    }
}