package com.pethiio.android

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class PethiioApplication : Application() {

    companion object {
        private var instance: Application? = null
        private var sharedPreferences: SharedPreferences? = null
        private var isActive: Boolean = false
        lateinit var context: Context


        fun applicationContext(): Context {
            return instance!!.applicationContext
        }

        fun getSharedPreference(): SharedPreferences? {
            return sharedPreferences
        }

        fun isActivityVisible(): Boolean {
            return isActive
        }
    }

    override fun onCreate() {
        super.onCreate()
        // initialize for any

        instance = this
        context = applicationContext()
//        registerActivityLifecycleCallbacks(this)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
    }



}