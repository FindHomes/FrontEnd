package com.homes.findhomes.domain.usecase.search

import com.homes.findhomes.data.model.SearchChatRequest
import com.homes.findhomes.data.model.SearchChatResponse
import com.homes.findhomes.domain.repository.SearchRepository
import javax.inject.Inject

class PostChatDataUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(searchChatRequest: SearchChatRequest) : SearchChatResponse? {
        return searchRepository.postChatData(searchChatRequest)
    }
}