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
            Log.d("401 호출됨", "401 호출됨")
            val token = getAccessToken(context)  // 기존 토큰 가져오기
            val newToken = refreshToken(token)  // 토큰 갱신 로직
            return if (newToken != null) {
                saveAccessToken(context, newToken)
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
            // Kakao 토큰 가져오기
            val kakaoToken = getKakaoToken(context)

            // 쿼리 파라미터로 Kakao 토큰을 URL에 추가
            val url = "http://3.39.75.112:8080/api/oauth/kakao?accessToken=$kakaoToken"

            val refreshTokenRequest = Request.Builder()
                .url(url)
                .get() // GET 요청
                .header("Authorization", oldToken ?: "")
                .build()

            val client = OkHttpClient()
            val response = client.newCall(refreshTokenRequest).execute()
            if (response.isSuccessful) {
                val body = response.body.string()
                return JSONObject(body).getString("token")
                Log.d("newAccessToken", JSONObject(body).getString("token").toString())
            } else {
                Log.d("newAccessToken 오류", response.toString())
                Log.d("newAccessToken 오류", response.body.toString())
                Log.d("newAccessToken 오류", response.message.toString())
                Log.d("newAccessToken 오류", response.code.toString())

            }
        } catch (e: Exception) {
            Log.e("TokenAuthenticator", "Failed to refresh token: ${e.localizedMessage}")
        }
        return null
    }
}
