package com.pethiio.android.data.model.resetPass

import com.google.gson.annotations.SerializedName

data class PinVerificationRequest(
    @SerializedName("pinValue")
    val pinValue: String = "",
    @SerializedName("referenceToken")
    val referenceToken: String = ""
)

