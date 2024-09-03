package com.example.findhomes.data

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

fun saveAccessToken(context: Context, token: String) {
    val preferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    val editor = preferences.edit()
    editor.putString("access_token", "Bearer "+"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhMjgwMmI0MS04YjAwLTQzZGYtYWQ3My1lYmMwZWQ0NjNkNzIiLCJpYXQiOjE3MjUzNDM1NjksImV4cCI6MTcyNTM0NzE2OX0.MzTqJlGZ510E5jkkAio5U5aW1NnQ3j7O40b13zFx8QU")
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
//    return preferences.getString("access_token", null)
    return "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhMjgwMmI0MS04YjAwLTQzZGYtYWQ3My1lYmMwZWQ0NjNkNzIiLCJpYXQiOjE3MjUzNDM1NjksImV4cCI6MTcyNTM0NzE2OX0.MzTqJlGZ510E5jkkAio5U5aW1NnQ3j7O40b13zFx8QU"
}
