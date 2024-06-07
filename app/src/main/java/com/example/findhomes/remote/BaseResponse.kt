package com.example.findhomes.remote

import com.google.gson.annotations.SerializedName

data class BaseResponse <T>(
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("code") val code : Int,
    @SerializedName("status") val status : Int,
    @SerializedName("message") val message : String,
    //T는 generic : 어떤 자료형을 명시하지 않아도 T로 쓰겠다 의미(result가 객체정보다 다 다르기 때문에)
    @SerializedName("result") val result : T,
)