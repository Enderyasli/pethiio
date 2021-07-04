package com.pethiio.android.data.model.report

import com.google.gson.annotations.SerializedName

data class ReportRequest(
    @SerializedName("description")
    val description: String = "",
    @SerializedName("reportedUserId")
    val reportedUserId: Int = 0,
    @SerializedName("type")
    val type: String = "",
)