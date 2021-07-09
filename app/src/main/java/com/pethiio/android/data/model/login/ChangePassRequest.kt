package com.pethiio.android.data.model.login

import com.google.gson.annotations.SerializedName

data class ChangePassRequest(
    @SerializedName("currentPassword")
    val currentPassword: String = "",
    @SerializedName("newPassword")
    val newPassword: String = ""
)
