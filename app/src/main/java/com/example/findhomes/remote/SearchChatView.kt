package com.example.findhomes.remote

interface SearchChatView {
    fun SearchChatLoading()
    fun SearchChatSuccess(content : SearchChatResponse)
    fun SearchChatFailure(code : Int, message : String)
}