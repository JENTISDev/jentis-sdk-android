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
    private val _sessionId = MutableLiveData<String?>()
    private val _sessiontimeInit = MutableLiveData<Long?>()
    val userId: LiveData<String?> get() = _userId
    val sessionId: LiveData<String?> get() = _sessionId

    val sessionTimeInit: LiveData<Long?> get() = _sessiontimeInit

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

    fun loadSessionTimeInit() {
        viewModelScope.launch {
            val sessionTimeInit = preferencesHelper.getSessionTime()
            _sessiontimeInit.postValue(sessionTimeInit)
        }
    }

    fun saveSessionId(sessionId: String) {
        viewModelScope.launch {
            preferencesHelper.saveSessionId(sessionId)
        }
    }

    fun saveSessionTime(timeInitSession: Long) {
        viewModelScope.launch {
            preferencesHelper.saveSessionTime(timeInitSession)
        }
    }

    fun loadSessionId() {
        viewModelScope.launch {
            val sessionId = preferencesHelper.getSessionId()
            _userId.postValue(sessionId)
        }
    }

    fun endSession() {
        viewModelScope.launch {
            preferencesHelper.endSession()
        }
    }
}
