package com.homes.findhomes.data.repository

import android.util.Log
import com.homes.findhomes.data.model.LogInResponse
import com.homes.findhomes.data.model.ManConRequest
import com.homes.findhomes.data.model.SearchCompleteResponse
import com.homes.findhomes.data.remote.LogInApi
import com.homes.findhomes.domain.repository.LogInRepository
import javax.inject.Inject

class LogInRepositoryImpl @Inject constructor(
    private val logInApi: LogInApi
) : LogInRepository {
    override suspend fun getLogInData(accessToken : String): LogInResponse? {
        return try {
            val response = logInApi.logIn(accessToken)
            if (response.success) {
                response.result
            } else {
                Log.e("logInData", response.message)
                null
            }
        } catch (e: Exception) {
            Log.e("logInData", "logIn", e)
            null
        }
    }
}