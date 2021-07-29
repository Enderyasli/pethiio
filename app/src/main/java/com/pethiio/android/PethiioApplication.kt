package com.pethiio.android

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController


class PethiioApplication : Application() {

    companion object {
        private var instance: Application? = null
        private var sharedPreferences: SharedPreferences? = null
        private var isActive: Boolean = false
        private var currentRoom: Int = 0
        @SuppressLint("StaticFieldLeak")
        private var navController: NavController? = null
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        fun getNavController(): NavController? {
            return navController
        }

        fun setNavController(navController: NavController) {
            this.navController = navController

        }


        fun setCurrentRoom(roomId: Int) {
            currentRoom = roomId
        }

        fun getCurrentRoom(): Int {
            return currentRoom
        }


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

    fun checkLogin() {

    }

    override fun onCreate() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        super.onCreate()
        // initialize for any

        instance = this
        context = applicationContext()
//        registerActivityLifecycleCallbacks(this)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)

//        ConnectSocketIO()
    }


}