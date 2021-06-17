package com.pawtind.android.data.api

import androidx.annotation.Nullable
import com.pawtind.android.data.model.AccessToken
import com.pawtind.android.utils.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {


    var interceptor = TokenInterceptor()

    private val client = OkHttpClient
        .Builder()
        .addInterceptor(interceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(RetrofitServices::class.java)

    fun buildService(): RetrofitServices {
        return retrofit
    }

    fun buildService(@Nullable accessToken: String): RetrofitServices {
        interceptor.accessToken = accessToken
        return retrofit
    }
}