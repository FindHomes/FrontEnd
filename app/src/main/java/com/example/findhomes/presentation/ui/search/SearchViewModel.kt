package com.example.findhomes.presentation.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findhomes.data.model.ManConRequest
import com.example.findhomes.data.model.SearchCompleteResponse
import com.example.findhomes.domain.usecase.search.GetSearchDataUseCase
import com.example.findhomes.domain.usecase.search.PostChatDataUseCase
import com.example.findhomes.domain.usecase.search.PostManConUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getSearchDataUseCase: GetSearchDataUseCase,
    private val postChatDataUseCase: PostChatDataUseCase,
    private val postManConUseCase: PostManConUseCase
): ViewModel() {
    private val _searchData = MutableLiveData<SearchCompleteResponse?>()
    val searchData: LiveData<SearchCompleteResponse?> = _searchData

    private val _chatData = MutableLiveData<String?>()
    val chatData : LiveData<String?> = _chatData

    private val _recommendData = MutableLiveData<List<String>?>()
    val recommendData : LiveData<List<String>?> = _recommendData

    fun loadSearchData() {
        viewModelScope.launch {
            _searchData.value = getSearchDataUseCase()
        }
    }

    fun postChatData(userInput : String) {
        viewModelScope.launch {
            val response = postChatDataUseCase(userInput)

        }
    }

    fun postManConData(manConRequest: ManConRequest){
        viewModelScope.launch {
            val response = postManConUseCase(manConRequest)
            _recommendData.postValue(response)
        }
    }
}
