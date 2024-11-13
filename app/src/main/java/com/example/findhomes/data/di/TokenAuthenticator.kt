package com.example.findhomes.data.di

import android.content.Context
import android.util.Log
import com.example.findhomes.data.getAccessToken
import com.example.findhomes.data.getKakaoToken
import com.example.findhomes.data.saveAccessToken
import okhttp3.Authenticator
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.Route
import org.json.JSONObject

class TokenAuthenticator(private val context: Context) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code == 401) {

            // 무한 루프 방지를 위해 요청 재시도 횟수 확인
            if (responseCount(response) >= 3) {
                Log.e("TokenAuthenticator", "Too many 401 follow-up requests.")
                return null
            }

            Log.d("401 호출됨", "401 호출됨")
            val token = getAccessToken(context)  // 기존 토큰 가져오기
            val newToken = refreshToken(token)  // 토큰 갱신 로직

            return if (newToken != null) {
                saveAccessToken(context, newToken)
                Log.d("TokenAuthenticator", "새 토큰으로 요청 생성: $newToken")

                // 갱신된 토큰을 직접 헤더에 추가하여 새 요청 생성
                response.request.newBuilder()
                    .header("Authorization", "Bearer $newToken")
                    .build()
            } else {
                null
            }
        }
        return null
    }

    private fun refreshToken(oldToken: String?): String? {
        try {
            val kakaoToken = getKakaoToken(context)
            val url = "http://3.39.75.112:8080/api/oauth/kakao?accessToken=$kakaoToken"

            val refreshTokenRequest = Request.Builder()
                .url(url)
                .get()
                .header("Authorization", oldToken ?: "")
                .build()

            val client = OkHttpClient()
            val response = client.newCall(refreshTokenRequest).execute()
            val body = response.body?.string()
            Log.d("TokenAuthenticator", "Response body: $body")

            if (response.isSuccessful) {
                val jsonObject = JSONObject(body)
                val resultObject = jsonObject.getJSONObject("result")
                val newToken = resultObject.getString("token")
                Log.d("newAccessToken", newToken)
                return newToken
            } else {
                Log.e("TokenAuthenticator", "Failed to refresh token. Response code: ${response.code}")
            }
        } catch (e: Exception) {
            Log.e("TokenAuthenticator", "토큰 갱신 실패: ${e.localizedMessage}")
        }
        return null
    }

    private fun responseCount(response: Response): Int {
        var currentResponse = response
        var count = 1
        while (currentResponse.priorResponse != null) {
            count++
            currentResponse = currentResponse.priorResponse!!
        }
        return count
    }
}
