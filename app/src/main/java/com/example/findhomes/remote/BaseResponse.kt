package com.example.findhomes.remote

import com.google.gson.annotations.SerializedName

data class BaseResponse <T> (
    @SerializedName("success") val success : Boolean,
    @SerializedName("code") val code : Int,
    @SerializedName("message") val message : String,
    @SerializedName("result") val result : T
)