package com.example.findhomes.remote

interface SearchCompleteView {
    fun SearchCompleteLoading()
    fun SearchCompleteSuccess(content : SearchCompleteResponse)
    fun SearchCompleteFailure(status : Int, message : String)
}