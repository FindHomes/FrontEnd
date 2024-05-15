package com.example.findhomes.local

import android.content.SharedPreferences
import com.example.findhomes.ApplicationClass

fun getJwt(): String {
    val prefs: SharedPreferences = ApplicationClass.mSharedPreferencesManager
    return prefs.getString("token", null) ?: ""
}
fun removeJwt() {
    ApplicationClass.mSharedPreferencesManager.edit().apply {
        remove("token")
        apply()
    }
}

fun saveJwt(token: String) {
    ApplicationClass.mSharedPreferencesManager.edit().apply {
        putString("token", token)
        apply()
    }
}