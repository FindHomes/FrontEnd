package com.homes.findhomes.domain.usecase.wish

import com.homes.findhomes.domain.repository.WishRepository
import javax.inject.Inject

class DeleteWishHistoryDataUseCase @Inject constructor(
private val wishRepository: WishRepository
) {
    suspend operator fun invoke(searchLogId : Int) : String? {
        return wishRepository.deleteWishHistory(searchLogId)
    }
}