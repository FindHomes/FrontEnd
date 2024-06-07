package com.example.findhomes.remote

interface SearchResultView {
    fun SearchResultLoading()
    fun SearchResultSuccess(content : ArrayList<SearchResultResponse>)
    fun SearchResultFailure(status : Int, message : String)

}