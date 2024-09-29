package com.example.findhomes.presentation.ui.login

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.common.util.Utility
import com.example.findhomes.databinding.ActivityLogInBinding

class LogInActivity : AppCompatActivity() {
    lateinit var binding : ActivityLogInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)

        var keyHash = Utility.getKeyHash(this)
        Log.d("keyHash", keyHash.toString())

        // 카카오 로그인 구현
        // ACTIVITY LAUNCHER로 CALL BACK 구현 예정

        setContentView(binding.root)
    }
}