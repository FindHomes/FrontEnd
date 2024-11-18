package com.homes.findhomes.presentation.ui.chat

data class ChatData(
    val message: String?,
    val isReceived: Boolean,
    var isLoading: Boolean = false
)