package com.pethiio.android.data.api

import androidx.annotation.Nullable
import com.androidnetworking.interceptors.HttpLoggingInterceptor
import com.pethiio.android.utils.Constants
import com.pethiio.android.utils.PreferenceHelper
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceBuilder {
    private var interceptor = TokenInterceptor()


//    var interceptor2: HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val client = OkHttpClient
        .Builder()
        .addInterceptor(getInterceptor())
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
//        .addInterceptor(interceptor2)
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
        getInterceptor().accessToken =
            PreferenceHelper.SharedPreferencesManager.getInstance().accessToken
        return retrofit
    }

    fun getInterceptor(): TokenInterceptor {
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor

    }
}