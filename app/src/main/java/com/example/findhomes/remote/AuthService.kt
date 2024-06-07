package com.example.findhomes.remote

import android.content.Context
import android.util.Log
import com.example.findhomes.ApplicationClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthService {
    private val authService = ApplicationClass.retrofit.create(RetrofitInterface::class.java)
    val essentialData : EssentialData ?= null
    private lateinit var searchResultView: SearchResultView

    fun setSearchResultView(searchResultView: SearchResultView){
        this.searchResultView = searchResultView
    }

    fun searchResultInfo(
        manCon : EssentialData,
        userInput : String
    ) {
        authService.searchResultInfo(SearchResultRequest(manCon, userInput))
            .enqueue(object : Callback<BaseResponse<ArrayList<SearchResultResponse>>> {
                override fun onResponse(
                    call: Call<BaseResponse<ArrayList<SearchResultResponse>>>,
                    response: Response<BaseResponse<ArrayList<SearchResultResponse>>>
                ) {
                    Log.d("search response", response.toString())
                    if (response.isSuccessful) {
                        val resp = response.body()
                        Log.d("search Response Body", resp.toString())
                        Log.d("search Response Body result", resp?.result.toString())
                        when (resp!!.status) {
                            200 -> searchResultView.SearchResultSuccess(resp.result)
                            else -> searchResultView.SearchResultFailure(
                                resp.status,
                                resp.message
                            )

                        }
                    }
                }

                override fun onFailure(

                    call: Call<BaseResponse<ArrayList<SearchResultResponse>>>,
                    t: Throwable
                ) {
                    Log.d("recentSearch Failed", t.toString())
                }

            })
    }


}