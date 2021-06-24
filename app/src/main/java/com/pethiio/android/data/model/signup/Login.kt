package com.pethiio.android.data.model.signup

import com.google.gson.annotations.SerializedName
import com.pethiio.android.data.model.LookUpsResponse
import com.pethiio.android.data.model.PawtindResponse

data class Login(

    @SerializedName("path")
    val path: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("fields")
    val fields: List<PawtindResponse>,
    @SerializedName("lookups")
    val lookups: List<LookUpsResponse>
)