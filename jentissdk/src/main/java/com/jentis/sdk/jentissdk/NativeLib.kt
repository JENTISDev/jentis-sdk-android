package com.jentis.sdk.jentissdk

class NativeLib {

    /**
     * A native method that is implemented by the 'jentissdk' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'jentissdk' library on application startup.
        init {
            System.loadLibrary("jentissdk")
        }
    }
}