package com.homes.findhomes.domain.usecase.wish

import com.homes.findhomes.data.model.SearchCompleteResponse
import com.homes.findhomes.data.model.WishHistoryResponse
import com.homes.findhomes.domain.repository.WishRepository
import javax.inject.Inject

class GetWishHistoryDataUseCase @Inject constructor(
    private val wishRepository: WishRepository
) {
    suspend operator fun invoke() : List<WishHistoryResponse>? {
        return wishRepository.getWishHistory()
    }
}