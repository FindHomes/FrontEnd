package com.example.findhomes.data

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

fun saveAccessToken(context: Context, token: String) {
    val preferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    val editor = preferences.edit()
//    editor.putString("access_token", "Bearer "+"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhMjgwMmI0MS04YjAwLTQzZGYtYWQ3My1lYmMwZWQ0NjNkNzIiLCJpYXQiOjE3MjYwNDM2NTgsImV4cCI6MTcyNjA0NzI1OH0.tVZyMNwPHgYTVZ0tST5Rk5Li1wl4MJvZErPWdB9DO8Y")
    editor.putString("access_token", "Bearer $token")
    editor.apply()
}

fun saveId(context: Context, userId: String) {
    val preferences = context.getSharedPreferences("app_preferences", AppCompatActivity.MODE_PRIVATE)
    val editor = preferences.edit()
    editor.putString("userId", userId)
    editor.apply()
}

fun getAccessToken(context: Context): String? {
    val preferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    return preferences.getString("access_token", null)
//    return "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhMjgwMmI0MS04YjAwLTQzZGYtYWQ3My1lYmMwZWQ0NjNkNzIiLCJpYXQiOjE3MjYwNDM2NTgsImV4cCI6MTcyNjA0NzI1OH0.tVZyMNwPHgYTVZ0tST5Rk5Li1wl4MJvZErPWdB9DO8Y"
}
