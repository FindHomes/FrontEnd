package com.example.findhomes.presentation.ui.login

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.findhomes.MainActivity
import com.example.findhomes.data.getAccessToken
import com.example.findhomes.data.saveAccessToken
import com.example.findhomes.data.saveKakaoToken
import com.kakao.sdk.common.util.Utility
import com.example.findhomes.databinding.ActivityLogInBinding
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogInActivity : AppCompatActivity() {
    lateinit var binding : ActivityLogInBinding
    private val viewModel: LogInViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // 카카오 로그인 구현
        binding.logInClKakao.setOnClickListener {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if(error != null){
                    Log.e("카카오 로그인", "카카오 로그인 실패 : ${error.message}")
                    if (error.toString().contains("KakaoTalk not installed")){
                        loginWithWeb()
                    }
                } else if(token!= null){
                    val kakaoAccessToken = token.accessToken
                    saveKakaoToken(this, kakaoAccessToken)
                    requestKakaoUserInfo()
                    Log.d("카카오 로그인", "카카오 로그인 성공")
                    Log.d("카카오 토큰", kakaoAccessToken)
                    requestKakaoUserInfo()
                    initLogin(kakaoAccessToken)
                }

            }
        }

        setContentView(binding.root)
    }

    private fun loginWithWeb() {
        UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
            if (error != null) {
                // 웹 브라우저를 통한 로그인 실패
                Log.e("카카오 로그인", "웹을 통한 로그인 실패: ${error.message}")
            } else if (token != null) {
                // 웹 브라우저를 통한 로그인 성공
                val kakaoAccessToken = token.accessToken
                saveKakaoToken(this, kakaoAccessToken)
                Log.d("카카오 로그인", "웹을 통한 로그인 성공")
                Log.d("카카오 토큰", kakaoAccessToken)
                requestKakaoUserInfo()
                initLogin(kakaoAccessToken)
            }
        }
    }

    private fun initLogin(kakaoToken : String) {
        viewModel.loadLogInData(kakaoToken)
        viewModel.logInData.observe(this){ token ->
            saveAccessToken(this, token!!.token)
        }
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun requestKakaoUserInfo() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                // 사용자 정보 요청 실패 처리
                Log.e(TAG, "사용자 정보 요청 실패: ${error.message}")
            } else if (user != null) {
                val userId = user.id
                val nickname = user.kakaoAccount?.profile?.nickname
                val isEmailVerified = user.kakaoAccount?.isEmailVerified ?: false

                // 이메일 미인증 시 동의창 띄우기
                if (!isEmailVerified) {
                    UserApiClient.instance.loginWithNewScopes(
                        this,
                        listOf("account_email")
                    ) { oAuthResponse, consentError ->
                        if (consentError != null) {
                            // 동의 실패 처리
                            Log.e(TAG, "동의 실패: ${consentError.message}")
                        } else {
                            // 동의 성공 처리
                            Log.i(TAG, "동의 성공")
                            // 동의창 띄운 후 추가 작업 수행
                            // 예를 들어, 사용자 정보 요청 등
                        }
                    }

                } else {
                    // 이미 이메일 인증된 사용자의 처리
                    Log.i(TAG, "이미 이메일 인증된 사용자")
                    // 추가 작업 수행
                }
            }
        }
    }
}