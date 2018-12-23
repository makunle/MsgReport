package com.noest.msgreport.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object Setting {

    lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun getString(key: String, def: String): String {
        return preferences.getString(key, def)
    }

    fun setString(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }

    fun getInt(key: String, def: Int): Int {
        return preferences.getInt(key, def)
    }

    fun setInt(key: String, value: Int) {
        preferences.edit().putInt(key, value).apply()
    }

    fun getBoolean(key: String, def: Boolean): Boolean {
        return preferences.getBoolean(key, def)
    }

    fun setBoolean(key: String, value: Boolean) {
        preferences.edit().putBoolean(key, value).apply()
    }
}