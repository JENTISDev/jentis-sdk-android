package com.jentis.sdk.jentissdk.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jentis.sdk.jentissdk.domain.local.usercase.GetUserIdUseCase
import com.jentis.sdk.jentissdk.domain.local.usercase.SaveUserIdUseCase
import kotlinx.coroutines.launch

class UserViewModel(
    private val saveUserIdUseCase: SaveUserIdUseCase,
    private val getUserIdUseCase: GetUserIdUseCase
) : ViewModel() {

    private val _userId = MutableLiveData<String?>()
    val userId: LiveData<String?> get() = _userId

    fun saveUserId(userId: String) {
        viewModelScope.launch {
            saveUserIdUseCase(userId)
        }
    }

    fun loadUserId() {
        viewModelScope.launch {
            val userID = getUserIdUseCase()
            _userId.postValue(userID)
        }
    }
}
