package com.example.findhomes.presentation.ui.wish

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findhomes.data.model.SearchCompleteResponse
import com.example.findhomes.data.model.WishHistoryResponse
import com.example.findhomes.domain.usecase.wish.GetWishFavoriteDataUseCase
import com.example.findhomes.domain.usecase.wish.GetWishHistoryDataUseCase
import com.example.findhomes.domain.usecase.wish.GetWishRecentDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishViewModel @Inject constructor(
    private val getFavoriteDataUseCase: GetWishFavoriteDataUseCase,
    private val getRecentDataUseCase: GetWishRecentDataUseCase,
    private val getWishHistoryDataUseCase: GetWishHistoryDataUseCase
): ViewModel(){
    private val _favoriteData = MutableLiveData<List<SearchCompleteResponse>?>()
    val favoriteData: LiveData<List<SearchCompleteResponse>?> = _favoriteData

    private val _recentData = MutableLiveData<List<SearchCompleteResponse>?>()
    val recentData: LiveData<List<SearchCompleteResponse>?> = _recentData

    private val _historyData = MutableLiveData<List<WishHistoryResponse>?>()
    val historyData: LiveData<List<WishHistoryResponse>?> = _historyData

    fun loadFavoriteData(){
        viewModelScope.launch{
            try {
                _favoriteData.value = getFavoriteDataUseCase()
                Log.d("WishViewModel", "로드된 데이터: ${_favoriteData.value}")
            } catch (e : Exception){
                Log.e("WishViewModel", "loadFavoriteData 오류", e)
            }
        }
    }

    fun loadRecentData(){
        viewModelScope.launch{
            try {
                _recentData.value = getRecentDataUseCase()
                Log.d("WishViewModel", "로드된 데이터: ${_recentData.value}")
            } catch (e : Exception){
                Log.e("WishViewModel", "loadRecentData 오류", e)
            }
        }
    }

    fun loadHistoryData(){
        viewModelScope.launch{
            try {
                _historyData.value = getWishHistoryDataUseCase()
                Log.d("WishViewModel", "로드된 데이터: ${_historyData.value}")
            } catch (e : Exception){
                Log.e("WishViewModel", "loadHistoryData 오류", e)
            }
        }
    }
}