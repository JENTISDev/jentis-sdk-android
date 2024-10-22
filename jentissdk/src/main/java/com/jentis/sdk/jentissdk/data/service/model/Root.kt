package com.jentis.sdk.jentissdk.data.service.model
data class Root(
    val system: System,
    val configuration: Configuration,
    val data: Data
)

data class System(
    val type: String,
    val timestamp: Long,
    val navigatorUserAgent: String,
    val initiator: String
)

data class Configuration(
    val container: String,
    val environment: String,
    val version: String,
    val debugcode: String
)

data class Data(
    val identifier: Identifier,
    val consent: Consent
)

data class Identifier(
    val user: User,
    val consent: ConsentIdentifier,
    val session : Session
)

data class User(
    val id: String,
    val action: String
)

data class Session(
    val id: String,
    val action: String
)

data class ConsentIdentifier(
    val id: String,
    val action: String
)

data class Consent(
    val lastupdate: Long,
    val data: Data?,
    val vendors: Vendors,
    val vendorsChanged: VendorsChanged
)

data class Vendors(
    val googleanalytics: Boolean,
    val facebook: String,
    val awin: Boolean
)

data class VendorsChanged(
    val facebook: String
)
