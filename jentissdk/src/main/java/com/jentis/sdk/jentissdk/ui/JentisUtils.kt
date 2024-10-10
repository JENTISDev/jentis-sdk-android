package com.jentis.sdk.jentissdk.ui

import kotlin.math.roundToInt

object JentisUtils {

    fun getNewUserID(): String {
        val sTime = System.currentTimeMillis()
        val sRand1 = (Math.random() * 100000).roundToInt()
        val sRand2 = (Math.random() * 100000).roundToInt()
        return "$sRand1$sTime$sRand2"
    }
}
