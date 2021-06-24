package com.pethiio.android.data.api

import com.pethiio.android.data.model.signup.Register

class ApiHelper(private val apiService: ApiService) {


    fun getLogin() = apiService.getLoginPageData()
    fun getRegister() = apiService.getRegisterPageData()
    fun getRegisterDetail() = apiService.getRegisterDetailPageData()
    fun getAnimalAddPhoto() = apiService.getAnimalAddPhotoPageData()
    fun getAnimalAdd() = apiService.getAnimalAddPageData()
    fun postRegister(register: Register) = apiService.postRegister(register)


}