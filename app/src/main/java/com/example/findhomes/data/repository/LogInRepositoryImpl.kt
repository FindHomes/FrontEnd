package com.example.findhomes.data.repository

import android.util.Log
import com.example.findhomes.data.model.LogInResponse
import com.example.findhomes.data.model.ManConRequest
import com.example.findhomes.data.model.SearchCompleteResponse
import com.example.findhomes.data.remote.LogInApi
import com.example.findhomes.domain.repository.LogInRepository
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