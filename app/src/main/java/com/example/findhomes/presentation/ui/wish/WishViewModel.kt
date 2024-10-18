package com.example.findhomes.presentation.ui.wish

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findhomes.data.model.SearchCompleteResponse
import com.example.findhomes.domain.usecase.wish.GetFavoriteDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishViewModel @Inject constructor(
    private val getFavoriteDataUseCase: GetFavoriteDataUseCase
): ViewModel(){
    private val _favoriteData = MutableLiveData<List<SearchCompleteResponse>?>()
    val favoriteData: LiveData<List<SearchCompleteResponse>?> = _favoriteData

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
}