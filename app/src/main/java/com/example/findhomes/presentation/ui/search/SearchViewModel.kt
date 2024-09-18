package com.example.findhomes.presentation.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findhomes.data.model.ManConRequest
import com.example.findhomes.data.model.SearchChatRequest
import com.example.findhomes.data.model.SearchChatResponse
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
    private val _searchData = MutableLiveData<List<SearchCompleteResponse>?>()
    val searchData: LiveData<List<SearchCompleteResponse>?> = _searchData
    private val _canLoadMore = MutableLiveData<Boolean>()
    val canLoadMore: LiveData<Boolean> = _canLoadMore

    private val _chatData = MutableLiveData<ChatData?>()
    val chatData : LiveData<ChatData?> = _chatData

    private val _recommendData = MutableLiveData<List<String>?>()
    val recommendData : LiveData<List<String>?> = _recommendData

    var currentMaxIndex = 20  // 초기에 로드할 아이템 수


    fun loadSearchData() {
        viewModelScope.launch {
            _searchData.value = getSearchDataUseCase()
            Log.d("searchData3", _searchData.value.toString())
        }
    }

    fun loadMoreData() {
        currentMaxIndex += 20  // 20개의 아이템을 더 로드
        _searchData.value?.let {
            _searchData.value = it
            updateCanLoadMore(it)
        }
    }

    private fun updateCanLoadMore(data: List<SearchCompleteResponse>?) {
        _canLoadMore.value = (data?.size ?: 0) > currentMaxIndex
    }

    fun postChatData(userInput : String) {
        viewModelScope.launch {
            val response = postChatDataUseCase(SearchChatRequest(userInput))
            _chatData.postValue(ChatData(response?.chatResponse, true))
        }
    }

    fun sendUserMessage(inputText: String) {
        _chatData.postValue(ChatData(inputText, false))
        postChatData(inputText)
    }

    fun postManConData(manConRequest: ManConRequest){
        viewModelScope.launch {
            val response = postManConUseCase(manConRequest)
            _recommendData.postValue(response)
        }
    }
}
