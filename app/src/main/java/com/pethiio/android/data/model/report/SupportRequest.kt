package com.pethiio.android.data.model.report

import com.google.gson.annotations.SerializedName

data class SupportRequest(
    @SerializedName("description")
    val description: String = "",
    @SerializedName("type")
    val type: String = "",
)