package com.pethiio.android.data.model.signup

import com.google.gson.annotations.SerializedName

data class Register(

    @SerializedName("email")
    val email: String = "",
    @SerializedName("firstName")
    val firstName: String = "",
    @SerializedName("lastName")
    val lastName: String = "",
    @SerializedName("password")
    val password: String = ""
)