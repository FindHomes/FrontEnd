package com.example.findhomes.presentation.ui.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.findhomes.MainActivity
import com.example.findhomes.R
import com.example.findhomes.data.getAccessToken

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            val preferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
            val isFirstLaunch = preferences.getBoolean("is_first_launch", false)
            Log.d("isFirstLaunch", "$isFirstLaunch")

            if(isFirstLaunch){
                val intent = Intent(this, LogInActivity::class.java)
                startActivity(intent)
            } else if(getAccessToken(this) == null){
                val intent = Intent(this, LogInActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            finish()
        }, 500)
    }
}