package com.pethiio.android.data.model.resetPass

import com.google.gson.annotations.SerializedName

data class ResetPassDemandResponse(
    @SerializedName("referenceToken")
    val referenceToken: String = ""
)

