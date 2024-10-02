package com.example.findhomes.domain.repository

import com.example.findhomes.data.model.LogInResponse
import com.example.findhomes.data.model.ManConRequest
import com.example.findhomes.data.model.SearchCompleteResponse

interface LogInRepository {
    suspend fun getLogInData(
        code : String
    ): LogInResponse?
}