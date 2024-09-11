package com.example.findhomes.domain.usecase.search

import com.example.findhomes.data.model.SearchChatRequest
import com.example.findhomes.data.model.SearchCompleteResponse
import com.example.findhomes.domain.repository.SearchRepository
import javax.inject.Inject

class PostChatDataUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(searchChatRequest: SearchChatRequest) : String? {
        return searchRepository.postChatData(searchChatRequest)
    }
}