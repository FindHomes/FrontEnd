package com.homes.findhomes.domain.repository

import com.homes.findhomes.data.model.LogInResponse
import com.homes.findhomes.data.model.ManConRequest
import com.homes.findhomes.data.model.SearchCompleteResponse

interface LogInRepository {
    suspend fun getLogInData(
        accessToken : String
    ): LogInResponse?
}