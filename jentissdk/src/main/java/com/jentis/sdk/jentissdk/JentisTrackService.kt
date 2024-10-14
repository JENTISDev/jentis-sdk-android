package com.jentis.sdk.jentissdk

import RootRepositoryImpl
import android.content.Context
import com.jentis.sdk.jentissdk.data.ApiClient
import com.jentis.sdk.jentissdk.domain.local.prefs.PreferencesHelper
import com.jentis.sdk.jentissdk.domain.network.usercases.SendRootDataUseCase
import com.jentis.sdk.jentissdk.ui.JentisUtils
import com.jentis.sdk.jentissdk.ui.lifecycle.CustomLifecycleOwner
import com.jentis.sdk.jentissdk.ui.viewmodel.ConsentViewModel
import com.jentis.sdk.jentissdk.ui.viewmodel.UserViewModel

class JentisTrackService private constructor(context: Context) {
    private val contextFinal: Context = context.applicationContext
    private lateinit var userViewModel: UserViewModel
    private var userId = ""

    private fun init() {
        val preferencesHelper = PreferencesHelper(contextFinal)

        userViewModel = UserViewModel(preferencesHelper)

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

    fun setConsent() {
        setConsentId()
    }

    private fun setConsentId() {
        val rootRepository = RootRepositoryImpl(ApiClient.api)
        val sendRootDataUseCase = SendRootDataUseCase(rootRepository)
        val preferencesHelper = PreferencesHelper(contextFinal)

        val consentViewModel =
            ConsentViewModel(preferencesHelper, sendRootDataUseCase)

        val customLifecycleOwner = CustomLifecycleOwner()

        consentViewModel.consentId.observe(customLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                val consentId = JentisUtils.getNewConsentID()
                saveConsentId(consentId, consentViewModel)
            }
        }

        consentViewModel.loadConsentId()
        customLifecycleOwner.handleStart()
    }

    private fun saveUserId(userId: String) {
        userViewModel.saveUserId(userId)
    }

    private fun saveConsentId(consentId: String, consentViewModel: ConsentViewModel) {
        consentViewModel.sendRootData(consentId, getUserId())
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
                val instance = JentisTrackService(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }

        fun getInstance(): JentisTrackService {
            return INSTANCE ?: throw IllegalStateException("JentisTrackService must be initialized")
        }
    }
}
