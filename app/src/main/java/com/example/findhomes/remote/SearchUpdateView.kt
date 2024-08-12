package com.example.findhomes.remote

interface SearchUpdateView {
    fun SearchUpdateLoading()
    fun SearchUpdateSuccess(content : SearchUpdateResponse)
    fun SearchUpdateFailure(status : Int, message : String)
}