package com.example.findhomes.presentation.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findhomes.data.model.SearchCompleteResponse
import com.example.findhomes.domain.usecase.search.GetSearchDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getSearchDataUseCase: GetSearchDataUseCase
): ViewModel() {
    private val _searchData = MutableLiveData<SearchCompleteResponse?>()
    val searchData: LiveData<SearchCompleteResponse?> = _searchData

    fun loadSearchData() {
        viewModelScope.launch {
            _searchData.value = getSearchDataUseCase()
        }
    }
}
