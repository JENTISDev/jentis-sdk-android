package com.jentis.sdk.jentissdk.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jentis.sdk.jentissdk.data.service.model.Configuration
import com.jentis.sdk.jentissdk.data.service.model.Consent
import com.jentis.sdk.jentissdk.data.service.model.ConsentIdentifier
import com.jentis.sdk.jentissdk.data.service.model.Data
import com.jentis.sdk.jentissdk.data.service.model.Identifier
import com.jentis.sdk.jentissdk.data.service.model.Root
import com.jentis.sdk.jentissdk.data.service.model.System
import com.jentis.sdk.jentissdk.data.service.model.User
import com.jentis.sdk.jentissdk.data.service.model.Vendors
import com.jentis.sdk.jentissdk.data.service.model.VendorsChanged
import com.jentis.sdk.jentissdk.domain.local.prefs.PreferencesHelper
import com.jentis.sdk.jentissdk.domain.network.usercases.SendRootDataUseCase
import kotlinx.coroutines.launch

class ConsentViewModel(
    private val preferencesHelper: PreferencesHelper,
    private val sendRootDataUseCase: SendRootDataUseCase
) : ViewModel() {

    private val _consentId = MutableLiveData<String?>()
    val consentId: LiveData<String?> get() = _consentId

    private val _sendRootDataState = MutableLiveData<Result<Unit>>()
    fun saveConsentId(consentId: String) {
        viewModelScope.launch {
                preferencesHelper.saveConsentId(consentId)
        }
    }

    fun loadConsentId() {
        viewModelScope.launch {
            val consentId = preferencesHelper.getConsentId()
            _consentId.postValue(consentId)
        }
    }

    fun sendRootData(consentId: String, userId: String) {
        val root = createMockedRoot(consentId, userId)

        viewModelScope.launch {
            val result = sendRootDataUseCase.execute(root)
            if (result.isSuccess) {
                saveConsentId(consentId)
                _sendRootDataState.postValue(result)
            }
        }
    }

    private fun createMockedRoot(consentId: String, userId: String): Root {
        // Mocking the System object
        val system = System(
            type = "app",
            timestamp = 1655704922774,
            navigatorUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.67 Safari/537.36",
            initiator = "jts_push_submit"
        )

        // Mocking the Configuration object
        val configuration = Configuration(
            container = "ckion-demo",
            environment = "live",
            version = "3",
            debugcode = "a675b5f1-48d2-43bf-b314-ba4830cda52d"
        )

        // Mocking the User object
        val user = User(
            id = userId,
            action = "new"
        )

        // Mocking the ConsentIdentifier object
        val consentIdentifier = ConsentIdentifier(
            id = consentId,
            action = "new"
        )

        // Mocking the Vendors object
        val vendors = Vendors(
            googleanalytics = true,
            facebook = "ncm",
            awin = false
        )

        // Mocking the VendorsChanged object
        val vendorsChanged = VendorsChanged(
            facebook = "ncm"
        )

        // Mocking the Consent object
        val consent = Consent(
            lastupdate = 1720611159546,
            data = Data(
                Identifier(user, consentIdentifier),
                Consent(
                    1720611159546,
                    null,
                    vendors = vendors,
                    vendorsChanged = vendorsChanged
                )
            ),
            vendors = vendors,
            vendorsChanged = vendorsChanged
        )

        // Mocking the Identifier object
        val identifier = Identifier(
            user = user,
            consent = consentIdentifier
        )

        // Mocking the Data object
        val data = Data(
            identifier = identifier,
            consent = consent
        )

        // Finally, create the Root object
        return Root(
            system = system,
            configuration = configuration,
            data = data
        )
    }
}
