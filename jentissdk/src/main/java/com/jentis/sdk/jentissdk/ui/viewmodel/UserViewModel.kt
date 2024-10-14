package com.jentis.sdk.jentissdk.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jentis.sdk.jentissdk.domain.local.prefs.PreferencesHelper
import kotlinx.coroutines.launch

class UserViewModel(
    private val preferencesHelper: PreferencesHelper
) : ViewModel() {

    private val _userId = MutableLiveData<String?>()
    val userId: LiveData<String?> get() = _userId

    fun saveUserId(userId: String) {
        viewModelScope.launch {
            preferencesHelper.saveUserId(userId)
        }
    }

    fun loadUserId() {
        viewModelScope.launch {
            val userID = preferencesHelper.getUserId()
            _userId.postValue(userID)
        }
    }
}
