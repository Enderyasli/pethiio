package com.pethiio.android.data.model.resetPass

import com.google.gson.annotations.SerializedName

data class ResetPassDemand(
    @SerializedName("email")
    val email: String = ""
)

