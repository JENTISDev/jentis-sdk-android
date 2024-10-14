package com.jentis.sdk.jentissdk.domain.local.prefs

import android.content.Context

class PreferencesHelper(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveUserId(userId: String) {
        val editor = sharedPreferences.edit()
        editor.putString(USER_ID_KEY, userId)
        editor.apply()
    }

    fun getUserId(): String? {
        return sharedPreferences.getString(USER_ID_KEY, null)
    }

    fun saveConsentId(consentId: String) {
        val editor = sharedPreferences.edit()
        editor.putString(CONSENT_ID_KEY, consentId)
        editor.apply()
    }

    fun getConsentId(): String? {
        return sharedPreferences.getString(CONSENT_ID_KEY, null)
    }

    fun clearData() {
        sharedPreferences.edit().clear().apply()
    }

    companion object {
        private const val PREF_NAME = "MyAppPreferences"
        private const val USER_ID_KEY = "userID"
        private const val CONSENT_ID_KEY = "consentID"
    }
}
