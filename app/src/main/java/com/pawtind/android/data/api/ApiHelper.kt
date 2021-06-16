package com.pawtind.android.data.api

import com.pawtind.android.data.model.signup.Register
import io.reactivex.Single

class ApiHelper(private val apiService: ApiService) {


    fun getLogin() = apiService.getLoginPageData()
    fun getRegister() = apiService.getRegisterPageData()
    fun getRegisterDetail() = apiService.getRegisterDetailPageData()
    fun getAnimalAddPhoto() = apiService.getAnimalAddPhotoPageData()
    fun getAnimalAdd() = apiService.getAnimalAddPageData()
    fun postRegister(register: Register) = apiService.postRegister(register)


}