package com.example.findhomes.domain.usecase.search

import com.example.findhomes.domain.repository.SearchRepository
import javax.inject.Inject

class PostSearchLogsDataUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke() : String? {
        return searchRepository.postSearchLogsData()
    }
}