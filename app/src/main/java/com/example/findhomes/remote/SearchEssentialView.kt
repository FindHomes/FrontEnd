package com.example.findhomes.remote

interface SearchEssentialView {
    fun SearchEssentialLoading()
    fun SearchEssentialSuccess(content : SearchEssentialResponse)
    fun SearchEssentialFailure(status : Int, message : String)
}