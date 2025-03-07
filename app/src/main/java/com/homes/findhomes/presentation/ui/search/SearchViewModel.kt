package com.homes.findhomes.presentation.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.homes.findhomes.data.model.ManConRequest
import com.homes.findhomes.data.model.SearchChatRequest
import com.homes.findhomes.data.model.SearchCompleteResponse
import com.homes.findhomes.data.model.SearchDetailResponse
import com.homes.findhomes.data.model.SearchStatisticsResponse
import com.homes.findhomes.domain.usecase.search.GetSearchDataUseCase
import com.homes.findhomes.domain.usecase.search.GetSearchDetailDataUseCase
import com.homes.findhomes.domain.usecase.search.GetSearchFavoriteDetailDataUseCase
import com.homes.findhomes.domain.usecase.search.GetSearchLogDataUseCase
import com.homes.findhomes.domain.usecase.search.GetSearchStatisticsUseCase
import com.homes.findhomes.domain.usecase.search.PostChatDataUseCase
import com.homes.findhomes.domain.usecase.search.PostManConUseCase
import com.homes.findhomes.domain.usecase.search.PostSearchFavoriteUseCase
import com.homes.findhomes.domain.usecase.search.PostSearchLogsDataUseCase
import com.homes.findhomes.presentation.ui.chat.ChatData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getSearchDataUseCase: GetSearchDataUseCase,
    private val getSearchLogDataUseCase: GetSearchLogDataUseCase,
    private val postChatDataUseCase: PostChatDataUseCase,
    private val postManConUseCase: PostManConUseCase,
    private val getSearchDetailDataUseCase: GetSearchDetailDataUseCase,
    private val getSearchDetailFavoriteUseCase: GetSearchFavoriteDetailDataUseCase,
    private val getSearchStatisticsUseCase: GetSearchStatisticsUseCase,
    private val postSearchFavoriteUseCase: PostSearchFavoriteUseCase,
    private val postSearchLogsDataUseCase: PostSearchLogsDataUseCase,
): ViewModel() {
    private val _searchData = MutableLiveData<List<SearchCompleteResponse>?>()
    val searchData: LiveData<List<SearchCompleteResponse>?> = _searchData
    private val _canLoadMore = MutableLiveData<Boolean>()
    val canLoadMore: LiveData<Boolean> = _canLoadMore

    private val _chatData = MutableLiveData<ChatData?>()
    val chatData : LiveData<ChatData?> = _chatData

    private val _recommendData = MutableLiveData<List<String>?>()
    val recommendData : LiveData<List<String>?> = _recommendData

    private val _detailData = MutableLiveData<SearchDetailResponse?>()
    val detailData: LiveData<SearchDetailResponse?> = _detailData

    private val _statsData = MutableLiveData<List<String>?>()
    val statsData: LiveData<List<String>?> = _statsData

    private val _statisticsData = MutableLiveData<List<SearchStatisticsResponse>?>()
    val statisticsData: LiveData<List<SearchStatisticsResponse>?> = _statisticsData

    private val _keywords = MutableLiveData<List<String>>()
    val keywords: LiveData<List<String>> = _keywords

    var currentMaxIndex = 20  // 초기에 로드할 아이템 수


    fun loadSearchData(manConRequest: ManConRequest) {
        viewModelScope.launch {
            try {
                Log.d("SearchViewModel", "데이터 로딩 시작")
                _searchData.value = getSearchDataUseCase(manConRequest)
                Log.d("SearchViewModel", "로드된 데이터: ${_searchData.value}")
            } catch (e : Exception){
                Log.e("SearchViewModel", "loadSearchData 오류", e)
            }
        }
    }

    fun loadSearchLogData(searchLogId : Int){
        viewModelScope.launch {
            try{
                Log.d("SearchViewModel", "데이터 로딩 시작")
                _searchData.value = getSearchLogDataUseCase(searchLogId)
                Log.d("SearchViewModel", "로드된 데이터: ${_searchData.value}")
            } catch (e : Exception){
                Log.e("SearchViewModel", "loadSearchData 오류", e)

            }
        }
    }

    fun loadSearchDetailData(houseId: Int){
        viewModelScope.launch {
            try {
                val response = getSearchDetailDataUseCase(houseId)
                if (response != null) {
                    _detailData.value = response.responseHouse
                    Log.d("SearchViewModel", "로드된 데이터: ${_detailData.value}")
                    _statsData.value = response.stats
                    Log.d("SearchViewModel", "로드된 데이터: ${_statsData.value}")
                } else {
                    Log.d("SearchViewModel", "No detail data found for houseId: $houseId")
                    _detailData.value = null
                }
            } catch (e: Exception){
                Log.e("SearchViewModel", "Error loading search detail data", e)
                _detailData.value = null
            }
        }
    }

    fun loadSearchFavoriteDetailData(houseId: Int){
        viewModelScope.launch {
            try {
                val response = getSearchDetailFavoriteUseCase(houseId)
                if (response != null) {
                    _detailData.value = response
                    Log.d("SearchViewModel", "로드된 데이터: ${_detailData.value}")
                    Log.d("SearchViewModel", "로드된 데이터: ${_statsData.value}")
                } else {
                    Log.d("SearchViewModel", "No detail data found for houseId: $houseId")
                    _detailData.value = null
                }
            } catch (e: Exception){
                Log.e("SearchViewModel", "Error loading search detail data", e)
                _detailData.value = null
            }
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
            _chatData.postValue(ChatData(response?.chatResponse, true, isLoading = false))
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

    fun postFavoriteData(houseId: Int, action: String){
        viewModelScope.launch {
            postSearchFavoriteUseCase(houseId, action)
        }
    }

    fun loadSearchStatisticsData() {
        viewModelScope.launch {
            try {
                val data = getSearchStatisticsUseCase()
                _statisticsData.value = data
                _keywords.value = data?.map { it.keyword } // 모든 키워드를 추출하여 저장
                Log.d("keywords", _keywords.value.toString())
                Log.d("SearchViewModel", "로드된 데이터: $data")
            } catch (e: Exception) {
                Log.e("SearchViewModel", "loadSearchStatisticsData 오류", e)
            }
        }
    }

    fun postSearchLogsData(){
        viewModelScope.launch {
            try{
                postSearchLogsDataUseCase()
                Log.d("SearchViewModel", "로그 잘 보내짐")
            } catch (e: Exception) {
                Log.e("SearchViewModel", "postSearchLogsData 오류", e)
            }
        }
    }
}
