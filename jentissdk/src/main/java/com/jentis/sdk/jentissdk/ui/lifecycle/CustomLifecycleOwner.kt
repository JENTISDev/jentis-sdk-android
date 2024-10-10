package com.jentis.sdk.jentissdk.ui.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

class CustomLifecycleOwner : LifecycleOwner {

    private val lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)

    init {
        lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
    }

    override val lifecycle: Lifecycle
        get() = lifecycleRegistry

    fun handleStart() {
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
    }

    fun handleStop() {
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
    }
}
