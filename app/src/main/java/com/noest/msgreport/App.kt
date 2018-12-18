package com.noest.msgreport

import android.app.Application
import com.noest.minicache.MiniCache

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        MiniCache.init(cacheDir.absolutePath)
    }
}