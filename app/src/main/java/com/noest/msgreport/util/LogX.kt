package com.noest.msgreport.util

import android.util.Log
import com.noest.msgreport.BuildConfig

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