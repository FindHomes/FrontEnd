package com.homes.findhomes.domain.usecase.wish

import com.homes.findhomes.data.model.SearchCompleteResponse
import com.homes.findhomes.domain.repository.WishRepository
import javax.inject.Inject

class GetWishRecentDataUseCase @Inject constructor(
    private val wishRepository: WishRepository
) {
    suspend operator fun invoke() : List<SearchCompleteResponse>? {
        return wishRepository.getWishRecent()
    }
}