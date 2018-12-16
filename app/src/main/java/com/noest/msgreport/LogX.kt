package com.noest.msgreport

import android.util.Log

object LogX {
    val PREFIX = "msgreport_"
    var mDebugging = BuildConfig.DEBUG

    fun setDebugging(debug: Boolean) {
        mDebugging = debug
    }

    fun d(tag: String, msg: String?) {
        if (mDebugging) {
            Log.d(PREFIX + tag, msg)
        }
    }
}