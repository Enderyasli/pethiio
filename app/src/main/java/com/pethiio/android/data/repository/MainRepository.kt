package com.pethiio.android.data.repository

import com.pethiio.android.data.api.ApiHelper
import com.pethiio.android.data.model.AccessToken
import com.pethiio.android.data.model.signup.Login
import com.pethiio.android.data.model.signup.Register
import io.reactivex.Single

class MainRepository(private val apiHelper: ApiHelper) {

    fun getLogin(): Single<Login> {
        return apiHelper.getLogin()
    }

    fun getRegister(): Single<Login> {
        return apiHelper.getRegister()
    }

    fun getRegisterDetail(): Single<Login> {
        return apiHelper.getRegisterDetail()
    }

    fun getAnimalAddPhoto(): Single<Login> {
        return apiHelper.getAnimalAddPhoto()
    }

    fun getAnimalAdd(): Single<Login> {
        return apiHelper.getAnimalAdd()
    }

    fun postRegister(register: Register): Single<AccessToken> {
        return apiHelper.postRegister(register)
    }

}