package com.pethiio.android.data.model.error

import com.google.gson.annotations.SerializedName

data class PethiioError(
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("subErrors")
    val subErrors: List<SubErrors>

)