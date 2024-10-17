package com.example.findhomes.presentation.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findhomes.data.model.LogInResponse
import com.example.findhomes.domain.usecase.logIn.GetLogInDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val getLogInDataUseCase : GetLogInDataUseCase
): ViewModel(){
    private val _logInData = MutableLiveData<LogInResponse?>()
    val logInData: LiveData<LogInResponse?> = _logInData

    fun loadLogInData(accessToken : String){
        viewModelScope.launch {
            try {
                val tokenData = getLogInDataUseCase(accessToken)
                _logInData.value = tokenData
                Log.d("SearchViewModel", "로드된 데이터: ${_logInData.value}")

            } catch (e : Exception){
                Log.e("LogInViewModel", "loadLogInData 오류", e)
            }
        }
    }
}