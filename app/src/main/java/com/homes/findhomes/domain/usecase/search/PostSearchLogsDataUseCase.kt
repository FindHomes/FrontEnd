package com.homes.findhomes.domain.usecase.search

import com.homes.findhomes.domain.repository.SearchRepository
import javax.inject.Inject

class PostSearchLogsDataUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke() : String? {
        return searchRepository.postSearchLogsData()
    }
}