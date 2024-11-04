package com.example.findhomes.domain.usecase.wish

import com.example.findhomes.domain.repository.WishRepository
import javax.inject.Inject

class DeleteWishHistoryDataUseCase @Inject constructor(
private val wishRepository: WishRepository
) {
    suspend operator fun invoke(searchLogId : Int) : String? {
        return wishRepository.deleteWishHistory(searchLogId)
    }
}