package com.jentis.sdk.jentissdk

import android.content.Context
import com.jentis.sdk.jentissdk.data.local.db.AppDatabase
import com.jentis.sdk.jentissdk.domain.local.repository.UserRepository
import com.jentis.sdk.jentissdk.domain.local.usercase.GetUserIdUseCase
import com.jentis.sdk.jentissdk.domain.local.usercase.SaveUserIdUseCase
import com.jentis.sdk.jentissdk.ui.JentisUtils
import com.jentis.sdk.jentissdk.ui.lifecycle.CustomLifecycleOwner
import com.jentis.sdk.jentissdk.ui.viewmodel.UserViewModel

class JentisTrackService private constructor(context: Context) {
    private lateinit var userViewModel: UserViewModel
    private val contextFinal = context
    private var userId = ""

    private fun init() {
        val userDao = AppDatabase.getDatabase(contextFinal).userDao()
        val userRepository = UserRepository(userDao)
        val saveUserIdUseCase = SaveUserIdUseCase(userRepository)
        val getUserIdUseCase = GetUserIdUseCase(userRepository)

        userViewModel = UserViewModel(saveUserIdUseCase, getUserIdUseCase)

        getUserId()

        /*
        this.sessionId = JentisUtils.getNewUserID()
        this.sessionCreatedAt = Date() */
    }

    /**
     * Initializes the Jentis Tracking in the SDK
     */
    fun initTracking() {
        init()
    }

    private fun saveUserId(userId: String) {
        userViewModel.saveUserId(userId)
    }

    fun getUserId(): String {
        val customLifecycleOwner = CustomLifecycleOwner()

        userViewModel.userId.observe(customLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                userId = JentisUtils.getNewUserID()
                saveUserId(userId)
            } else {
                userId = it.toString()
            }
        }

        userViewModel.loadUserId()
        customLifecycleOwner.handleStart()

        return userId
    }

    companion object {
        @Volatile
        private var INSTANCE: JentisTrackService? = null

        fun initialize(context: Context): JentisTrackService {
            return INSTANCE ?: synchronized(this) {
                val instance = JentisTrackService(context)
                INSTANCE = instance
                instance
            }
        }

        fun getInstance(): JentisTrackService {
            return INSTANCE ?: throw IllegalStateException("JentisTrackService must be initialized")
        }
    }
}
