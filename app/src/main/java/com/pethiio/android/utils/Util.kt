package com.pethiio.android.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.pethiio.android.PethiioApplication

public object PreferenceHelper {

    fun defaultPrefs(context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    fun customPrefs(context: Context, name: String): SharedPreferences =
        context.getSharedPreferences(name, Context.MODE_PRIVATE)

    inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }


    class SharedPreferencesManager {
        private val sharedPreferences: SharedPreferences =
            PethiioApplication.context.getSharedPreferences(
                MY_APP_PREFERENCES,
                Context.MODE_PRIVATE
            )

        public var isFirstDownload: Boolean?
            get() = sharedPreferences.getBoolean(IS_FIRST, true)
            set(value) = sharedPreferences.edit().putBoolean(IS_FIRST, value == true).apply()

        public var accessToken: String
            get() = sharedPreferences.getString(ACCESS_TOKEN, "").toString()
            set(value) = sharedPreferences.edit().putString(ACCESS_TOKEN, value).apply()

        public var petId: Int
            get() = sharedPreferences.getInt(PET_ID, 0)
            set(value) = sharedPreferences.edit().putInt(PET_ID, value).apply()

        public var isLoggedIn: Boolean?
            get() = sharedPreferences.getBoolean(IS_LOGGED_IN, false)
            set(value) = sharedPreferences.edit().putBoolean(IS_LOGGED_IN, value == true).apply()


        companion object {
            private const val MY_APP_PREFERENCES = "Pawtind-Android"
            private const val IS_FIRST = "isFirstDownload"
            private const val ACCESS_TOKEN = "accessToken"
            private const val IS_LOGGED_IN = "loginState"
            private const val PET_ID = "pet_id"
            private var instance: SharedPreferencesManager? = null

            @Synchronized
            fun getInstance(): SharedPreferencesManager {
                if (instance == null) instance = SharedPreferencesManager()
                return instance!!
            }
        }
    }
}