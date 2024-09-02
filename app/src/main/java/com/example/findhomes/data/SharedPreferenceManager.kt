package com.example.findhomes.data

import android.content.SharedPreferences
import com.example.findhomes.ApplicationClass

fun getJwt() : String{
    val prefs: SharedPreferences = ApplicationClass.mSharedPreferences
//    return prefs.getString("token", null) ?: ""
    return "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhMjgwMmI0MS04YjAwLTQzZGYtYWQ3My1lYmMwZWQ0NjNkNzIiLCJpYXQiOjE3MjQ4NTA2MjcsImV4cCI6MTcyNDg1NDIyN30.oCa6X9pRlN4b7hLrkQYqaHS_wrRC0XHcH3dvcG36cwM"
}

fun removeJwt(){
    ApplicationClass.mSharedPreferences.edit().apply{
        remove("token")
        apply()
    }
}

fun saveJwt(token : String){
    ApplicationClass.mSharedPreferences.edit().apply{
        putString("token", token)
        apply()
    }
}