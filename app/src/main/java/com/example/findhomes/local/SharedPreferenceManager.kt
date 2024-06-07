package com.example.findhomes.local

import android.content.SharedPreferences
import android.system.Os.remove
import com.example.findhomes.ApplicationClass

fun getJwt(): String {
    val prefs: SharedPreferences = ApplicationClass.mSharedPreferences
    return prefs.getString("token", null) ?: ""
}
fun removeJwt() {
    ApplicationClass.mSharedPreferences.edit().apply {
        remove("token")
        apply()
    }
}

fun saveJwt(token: String) {
    ApplicationClass.mSharedPreferences.edit().apply {
        putString("token", token)
        apply()
    }
}