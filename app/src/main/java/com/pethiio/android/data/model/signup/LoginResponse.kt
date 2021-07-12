package com.pethiio.android.data.model.signup

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("accessToken")
    val accessToken: String = "",
    @SerializedName("emailVerified")
    val emailVerified: Boolean = false,
    @SerializedName("registrationCompleted")
    val registrationCompleted: Boolean = false,
    @SerializedName("emailVerificationToken")
    val emailVerificationToken: String = "",


)