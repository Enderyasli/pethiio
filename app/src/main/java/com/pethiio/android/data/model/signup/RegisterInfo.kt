package com.pethiio.android.data.model.signup

import com.google.gson.annotations.SerializedName

data class RegisterInfo(

    @SerializedName("aboutMe")
    val aboutMe: String = "",
    @SerializedName("dateOfBirth")
    val dateOfBirth: String = "",
    @SerializedName("gender")
    val gender: String = "",
)