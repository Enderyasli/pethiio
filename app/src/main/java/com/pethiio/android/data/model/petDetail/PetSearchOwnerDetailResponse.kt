package com.pethiio.android.data.model.petDetail

import com.google.gson.annotations.SerializedName
import com.pethiio.android.data.model.PethiioResponse

class PetSearchOwnerDetailResponse(
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("age")
    val age: String,
    @SerializedName("about")
    val about: String,

    )

