package com.example.findhomes.remote

import android.util.Log
import com.example.findhomes.ApplicationClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthService() {

    private val authService = ApplicationClass.retrofit.create(RetrofitInterface::class.java)

    private lateinit var searchUpdateView: SearchUpdateView
    private lateinit var searchCompleteView: SearchCompleteView


    fun setSearchUpdateView(searchUpdateView: SearchUpdateView) {
        this.searchUpdateView = searchUpdateView
    }
    fun setSearchCompleteView(searchCompleteView: SearchCompleteView) {
        this.searchCompleteView = searchCompleteView
    }

    fun searchComplete(){
        authService.searchUpdate()
            .enqueue(object : Callback<BaseResponse<SearchCompleteResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<SearchCompleteResponse>>,
                    response: Response<BaseResponse<SearchCompleteResponse>>
                ) {
//                    Log.d("bringUser response", response.toString())
                    if (response.isSuccessful) {
                        val resp = response.body()
//                        Log.d("bringUser Response Body", resp.toString())
//                        Log.d("bringUser Response Body result", resp?.result.toString())
                        when (resp!!.status) {
                            200 -> searchCompleteView.SearchCompleteSuccess(resp.result)
                            else -> searchCompleteView.SearchCompleteFailure(resp.status, resp.message)
                        }
                    }
                }

                override fun onFailure(call: Call<BaseResponse<SearchCompleteResponse>>, t: Throwable) {
                    Log.d("SearchComplete Failed", t.toString())
                }
            })
    }

    fun searchUpdate(xMax: Double, xMin : Double, yMax: Double, yMin: Double){
        authService.searchUpdate(xMax, xMin, yMax, yMin)
            .enqueue(object : Callback<BaseResponse<SearchUpdateResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<SearchUpdateResponse>>,
                    response: Response<BaseResponse<SearchUpdateResponse>>
                ) {
//                    Log.d("bringUser response", response.toString())
                    if (response.isSuccessful) {
                        val resp = response.body()
//                        Log.d("bringUser Response Body", resp.toString())
//                        Log.d("bringUser Response Body result", resp?.result.toString())
                        when (resp!!.status) {
                            200 -> searchUpdateView.SearchUpdateSuccess(resp.result)
                            else -> searchUpdateView.SearchUpdateFailure(resp.status, resp.message)
                        }
                    }
                }

                override fun onFailure(call: Call<BaseResponse<SearchUpdateResponse>>, t: Throwable) {
                    Log.d("SearchUpdate Failed", t.toString())
                }
            })
    }
}