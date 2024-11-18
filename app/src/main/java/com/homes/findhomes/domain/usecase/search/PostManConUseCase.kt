package com.homes.findhomes.domain.usecase.search

import com.homes.findhomes.data.model.ManConRequest
import com.homes.findhomes.domain.repository.SearchRepository
import javax.inject.Inject

class PostManConUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(manConRequest: ManConRequest) : List<String>? {
        return searchRepository.postManConData(manConRequest)
    }
}