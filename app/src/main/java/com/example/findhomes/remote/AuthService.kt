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
    private lateinit var searchChatView : SearchChatView
    private lateinit var searchEssentialView : SearchEssentialView


    fun setSearchUpdateView(searchUpdateView: SearchUpdateView) {
        this.searchUpdateView = searchUpdateView
    }
    fun setSearchCompleteView(searchCompleteView: SearchCompleteView) {
        this.searchCompleteView = searchCompleteView
    }

    fun setSearchChatView(searchChatView: SearchChatView){
        this.searchChatView = searchChatView
    }

    fun setSearchEssentialView(searchEssentialView : SearchEssentialView){
        this.searchEssentialView = searchEssentialView
    }

    fun searchComplete(){
        authService.searchComplete()
            .enqueue(object : Callback<BaseResponse<SearchCompleteResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<SearchCompleteResponse>>,
                    response: Response<BaseResponse<SearchCompleteResponse>>
                ) {
                    Log.d("SearchComplete response", response.toString())
                    if (response.isSuccessful) {
                        val resp = response.body()
                        Log.d("SearchComplete Response Body", resp.toString())
                        when (resp!!.code) {
                            200 -> searchCompleteView.SearchCompleteSuccess(resp.result)
                            else -> searchCompleteView.SearchCompleteFailure(resp.code, resp.message)
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
                        when (resp!!.code) {
                            200 -> searchUpdateView.SearchUpdateSuccess(resp.result)
                            else -> searchUpdateView.SearchUpdateFailure(resp.code, resp.message)
                        }
                    }
                }

                override fun onFailure(call: Call<BaseResponse<SearchUpdateResponse>>, t: Throwable) {
                    Log.d("SearchUpdate Failed", t.toString())
                }
            })
    }

    fun searchChat(usersInput : String){
        val request = SearchChatRequest(usersInput)
        authService.searchChat(request)
            .enqueue(object : Callback<BaseResponse<SearchChatResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<SearchChatResponse>>,
                    response: Response<BaseResponse<SearchChatResponse>>
                ) {
                    Log.d("SearchChat response", response.toString())
                    if (response.isSuccessful) {
                        val resp = response.body()
                        Log.d("SearchChat Response Body", resp.toString())
                        when (resp!!.code) {
                            200 -> searchChatView.SearchChatSuccess(resp.result)
                            else -> searchChatView.SearchChatFailure(resp.code, resp.message)
                        }
                    }
                }

                override fun onFailure(call: Call<BaseResponse<SearchChatResponse>>, t: Throwable) {
                    Log.d("SearchChat Failed", t.toString())
                }
            })
    }

    fun searchEssentialView(housingTypes : List<String>, prices : PricesInfo, region : String){
        val request = SearchEssentialRequest(housingTypes, prices, region)
        authService.searchEssential(request)
            .enqueue(object : Callback<BaseResponse<SearchEssentialResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<SearchEssentialResponse>>,
                    response: Response<BaseResponse<SearchEssentialResponse>>
                ) {
                    Log.d("SearchChat response", response.toString())
                    if (response.isSuccessful) {
                        val resp = response.body()
                        Log.d("SearchChat Response Body", resp.toString())
                        when (resp!!.code) {
                            200 -> searchEssentialView.SearchEssentialSuccess(resp.result)
                            else -> searchEssentialView.SearchEssentialFailure(resp.code, resp.message)
                        }
                    }
                }

                override fun onFailure(call: Call<BaseResponse<SearchEssentialResponse>>, t: Throwable) {
                    Log.d("SearchChat Failed", t.toString())
                }
            })
    }
}