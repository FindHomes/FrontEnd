package com.example.findhomes.domain.usecase.wish

import com.example.findhomes.data.model.SearchCompleteResponse
import com.example.findhomes.data.model.WishHistoryResponse
import com.example.findhomes.domain.repository.WishRepository
import javax.inject.Inject

class GetWishHistoryDataUseCase @Inject constructor(
    private val wishRepository: WishRepository
) {
    suspend operator fun invoke() : List<WishHistoryResponse>? {
        return wishRepository.getWishHistory()
    }
}