package com.noest.msgreport

import android.app.Application
import com.noest.msgreport.util.Setting

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Setting.init(this)
    }
}