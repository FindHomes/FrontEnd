package com.example.findhomes.domain.usecase.logIn

import com.example.findhomes.data.model.LogInResponse
import com.example.findhomes.domain.repository.LogInRepository
import javax.inject.Inject

class GetLogInDataUseCase @Inject constructor(
    private val logInRepository: LogInRepository
) {
    suspend operator fun invoke(code : String) : LogInResponse? {
        return logInRepository.getLogInData(code)
    }
}