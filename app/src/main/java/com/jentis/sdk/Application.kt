package com.jentis.sdk

import android.app.Application
import com.jentis.sdk.jentissdk.JentisTrackService

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        JentisTrackService.initialize(applicationContext)

        val container = "ckion-demo"
        val environment = "live"
        val version = "3"
        val debugCode = "a675b5f1-48d2-43bf-b314-ba4830cda52d"
        val trackDomain = "https://qc3ipx.ckion-dev.jtm-demo.com"

        JentisTrackService.getInstance().initTracking(
            application = this,
            trackDomain = trackDomain,
            container = container,
            environment = environment,
            version = version,
            debugCode = debugCode
        )
    }
}