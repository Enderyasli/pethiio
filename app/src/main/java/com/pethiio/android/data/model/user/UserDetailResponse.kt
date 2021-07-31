package com.pethiio.android.data.model.user

import com.google.gson.annotations.SerializedName

class UserDetailResponse(
    @SerializedName("aboutMe")
    val aboutMe: String,
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("dateOfBirth")
    val dateOfBirth: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("lastName")
    val lastName: String
)
