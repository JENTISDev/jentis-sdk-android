package com.jentis.sdk.jentissdk

import RootRepositoryImpl
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.jentis.sdk.jentissdk.data.ApiClient
import com.jentis.sdk.jentissdk.domain.local.prefs.PreferencesHelper
import com.jentis.sdk.jentissdk.domain.network.usercases.SendRootDataUseCase
import com.jentis.sdk.jentissdk.ui.JentisUtils
import com.jentis.sdk.jentissdk.ui.lifecycle.CustomLifecycleOwner
import com.jentis.sdk.jentissdk.ui.viewmodel.ConsentViewModel
import com.jentis.sdk.jentissdk.ui.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class JentisTrackService private constructor(context: Context) :
    Application.ActivityLifecycleCallbacks {

    private val contextFinal: Context = context.applicationContext
    private lateinit var userViewModel: UserViewModel
    private var userId = ""
    private var consentId = ""
    private var activityReferences = 0
    private var isActivityChangingConfigurations = false
    private val SESSION_ACTION_NEW = "new"
    private val SESSION_ACTION_UPDATE = "update"
    private val SESSION_ACTION_END = "end"
    private val sessionTimeout = 30 * 60 * 1000 // Timeout de 30 minutos
    private val customLifecycleOwner = CustomLifecycleOwner()
    private var CONTAINER = ""
    private var ENVIRONMENT = ""
    private var VERSION = ""
    private var DEBUG_CODE = ""
    private var TRACK_DOMAIN = ""

    private fun init() {
        val preferencesHelper = PreferencesHelper(contextFinal)
        userViewModel = UserViewModel(preferencesHelper)
        getUserId()
    }

    /**
     * Initializes the Jentis Tracking in the SDK
     */
    fun initTracking(
        application: Application,
        trackDomain: String,
        container: String,
        environment: String,
        version: String,
        debugCode: String
    ) {
        TRACK_DOMAIN = trackDomain
        CONTAINER = container
        ENVIRONMENT = environment
        VERSION = version
        DEBUG_CODE = debugCode
        application.registerActivityLifecycleCallbacks(this)
        init()
    }

    private fun saveSessionId(sessionId: String, sessionAction: String) {
        val session = "$sessionId#$sessionAction"
        userViewModel.saveSessionId(session)
        saveSessionTime()
        val rootRepository =
            RootRepositoryImpl(ApiClient.create(context = contextFinal, trackDomain = TRACK_DOMAIN))
        val sendRootDataUseCase = SendRootDataUseCase(rootRepository)
        val preferencesHelper = PreferencesHelper(contextFinal)

        val consentViewModel =
            ConsentViewModel(preferencesHelper, sendRootDataUseCase)

        consentViewModel.sendRootData(
            consentId = consentId,
            userId = getUserId(),
            sessionId = sessionId,
            sessionAction = sessionAction,
            container = CONTAINER,
            environment = ENVIRONMENT,
            version = VERSION,
            debugCode = DEBUG_CODE
        )
    }

    private fun saveSessionTime() {
        // save time locally
        val timeInitSession = System.currentTimeMillis()
        userViewModel.saveSessionTime(timeInitSession)
    }

    private fun getUserId(): String {
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

    private fun saveUserId(userId: String) {
        userViewModel.saveUserId(userId)
    }

    private fun endSession() {
        CoroutineScope(Dispatchers.Default).launch {
            saveSessionId(JentisUtils.getNewSessionID(), SESSION_ACTION_END)
        }
    }

    fun setConsent() {
        setConsentId()
    }

    private fun setConsentId() {
        val rootRepository =
            RootRepositoryImpl(ApiClient.create(context = contextFinal, trackDomain = TRACK_DOMAIN))
        val sendRootDataUseCase = SendRootDataUseCase(rootRepository)
        val preferencesHelper = PreferencesHelper(contextFinal)

        val consentViewModel =
            ConsentViewModel(preferencesHelper, sendRootDataUseCase)

        consentViewModel.consentId.observe(customLifecycleOwner) {
            consentId = it.toString()

            if (it.isNullOrEmpty()) {
                consentId = JentisUtils.getNewConsentID()
            }

            saveSessionId(JentisUtils.getNewSessionID(), SESSION_ACTION_NEW)
        }

        consentViewModel.loadConsentId()
        customLifecycleOwner.handleStart()
    }

    private fun getSessionId(): String {
        val customLifecycleOwner = CustomLifecycleOwner()
        var parts = ""

        userViewModel.sessionId.observe(customLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                parts = it.toString()
            }
        }

        userViewModel.loadSessionId()
        customLifecycleOwner.handleStart()

        return parts
    }

    override fun onActivityStarted(activity: Activity) {
        if (activityReferences == 0 && !isActivityChangingConfigurations) {
            userViewModel.loadSessionTimeInit()

            userViewModel.sessionTimeInit.observe(customLifecycleOwner) { timeInit ->
                if (timeInit != null && timeInit > 0) {
                    val timeInBackground = System.currentTimeMillis() - timeInit

                    if (timeInBackground >= sessionTimeout) {
                        endSession()
                        saveSessionId(JentisUtils.getNewSessionID(), SESSION_ACTION_NEW)
                    } else {
                        saveSessionId(JentisUtils.getNewSessionID(), SESSION_ACTION_UPDATE)
                    }
                }
            }
        }
        activityReferences++
    }

    override fun onActivityStopped(activity: Activity) {
        activityReferences--
        isActivityChangingConfigurations = activity.isChangingConfigurations

        if (activityReferences == 0 && !isActivityChangingConfigurations) {
            saveSessionTime()
        }
    }

    override fun onActivityDestroyed(activity: Activity) {
        endSession()
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

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
