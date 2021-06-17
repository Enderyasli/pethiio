package com.pawtind.android.data.model.signup

import com.google.gson.annotations.SerializedName
import com.pawtind.android.data.model.PawtindResponse

data class RegisterInfo(

    @SerializedName("aboutMe")
    val aboutMe: String = "",
    @SerializedName("dateOfBirth")
    val dateOfBirth: String = "",
    @SerializedName("gender")
    val gender: String = "",
)