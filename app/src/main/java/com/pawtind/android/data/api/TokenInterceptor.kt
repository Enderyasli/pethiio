package com.pawtind.android.data.api

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.http.Headers
import java.io.IOException


class TokenInterceptor : Interceptor {

    lateinit var accessToken: String
    override fun intercept(chain: Interceptor.Chain): Response {

        //rewrite the request to add bearer token
        val newRequest: Request = chain.request().newBuilder()
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer $accessToken")
            .build()
        return chain.proceed(newRequest)
    }


}