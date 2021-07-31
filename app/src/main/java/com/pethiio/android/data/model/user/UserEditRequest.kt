package com.pethiio.android.data.model.user

import com.google.gson.annotations.SerializedName

class UserEditRequest(
    @SerializedName("aboutMe")
    val aboutMe: String,
    @SerializedName("dateOfBirth")
    val dateOfBirth: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("lastName")
    val lastName: String
)
