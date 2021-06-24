package com.pethiio.android.data.model.signup

import com.google.gson.annotations.SerializedName

data class LoginInfoMapper(

    @SerializedName("emailPlaceholder")
    var emailPlaceholder: String = "emailPlaceholder",
    @SerializedName("emailTitle")
    val emailTitle: String = "emailTitle",
    @SerializedName("forgotPasswordButtonTitle")
    val forgotPasswordButtonTitle: String = "forgotPasswordButtonTitle",
    @SerializedName("loginButtonTitle")
    val loginButtonTitle: String = "loginButtonTitle",
    @SerializedName("passwordPlaceholder")
    val passwordPlaceholder: String = "passwordPlaceholder",
    @SerializedName("passwordTitle")
    val passwordTitle: String = "passwordTitle",
    @SerializedName("title")
    val title: String = "title",


    )