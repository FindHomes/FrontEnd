package com.homes.findhomes.domain.usecase.logIn

import com.homes.findhomes.data.model.LogInResponse
import com.homes.findhomes.domain.repository.LogInRepository
import javax.inject.Inject

class GetLogInDataUseCase @Inject constructor(
    private val logInRepository: LogInRepository
) {
    suspend operator fun invoke(accessToken : String) : LogInResponse? {
        return logInRepository.getLogInData(accessToken)
    }
}