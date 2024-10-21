package com.example.findhomes.domain.usecase.wish

import com.example.findhomes.data.model.SearchCompleteResponse
import com.example.findhomes.domain.repository.WishRepository
import javax.inject.Inject

class GetWishRecentDataUseCase @Inject constructor(
    private val wishRepository: WishRepository
) {
    suspend operator fun invoke() : List<SearchCompleteResponse>? {
        return wishRepository.getWishRecent()
    }
}