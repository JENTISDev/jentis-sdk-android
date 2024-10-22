package com.jentis.sdk.jentissdk.ui.workmanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class SessionWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        performSDKBackgroundTask()
        return Result.success()
    }

    private fun performSDKBackgroundTask() {
    }
}
