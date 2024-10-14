package com.jentis.sdk.jentissdk.ui

import kotlin.math.roundToInt

object JentisUtils {

    fun getNewUserID(): String {
        val sTime = System.currentTimeMillis()
        val sRand1 = (Math.random() * 100000).roundToInt()
        val sRand2 = (Math.random() * 100000).roundToInt()
        return "$sRand1$sTime$sRand2"
    }

    fun getNewConsentID(): String {
        return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(Regex("[xy]")) { matchResult ->
            val char = matchResult.value
            val r = (Math.random() * 16).toInt()
            val v = if (char == "x") r else (r and 0x3 or 0x8)
            v.toString(16)
        }
    }
}
