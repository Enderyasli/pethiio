package com.pethiio.android.data.model.resetPass

import com.google.gson.annotations.SerializedName

data class ResetPasswordRequest(
    @SerializedName("password")
    val password: String = "",
    @SerializedName("pinValue")
    val pinValue: String = "",
    @SerializedName("referenceToken")
    val referenceToken: String = ""
)

