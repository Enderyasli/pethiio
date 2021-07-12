package com.pethiio.android.data.model.signup

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("accessToken")
    val accessToken: String = "",
    @SerializedName("emailVerificationToken")
    val emailVerificationToken: String = ""
)