package com.pethiio.android.data.model.error

import com.google.gson.annotations.SerializedName

data class SubErrors(
    @SerializedName("field")
    val field: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("rejectedValue")
    val rejectedValue: String

)