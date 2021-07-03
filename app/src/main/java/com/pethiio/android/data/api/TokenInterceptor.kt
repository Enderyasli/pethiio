package com.pethiio.android.data.api

import com.androidnetworking.interceptors.HttpLoggingInterceptor
import com.pethiio.android.utils.PreferenceHelper
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


class TokenInterceptor : Interceptor {

    lateinit var accessToken: String
    override fun intercept(chain: Interceptor.Chain): Response {

        //rewrite the request to add bearer token
        val newRequest: Request = chain.request().newBuilder()
            .header("Content-Type", "application/json")
            .header(
                "Accept-Language",
                "tr"
//                PreferenceHelper.SharedPreferencesManager.getInstance().appLanguage
            )
            .header("Authorization", "Bearer $accessToken")
            .build()
        return chain.proceed(newRequest)
    }


}