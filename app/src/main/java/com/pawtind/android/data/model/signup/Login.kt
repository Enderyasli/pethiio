package com.pawtind.android.data.model.signup

import com.google.gson.annotations.SerializedName
import com.pawtind.android.data.model.LookUpsResponse
import com.pawtind.android.data.model.PawtindResponse

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