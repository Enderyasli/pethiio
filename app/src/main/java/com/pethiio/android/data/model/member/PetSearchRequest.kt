package com.pethiio.android.data.model.member

import com.google.gson.annotations.SerializedName

data class PetSearchRequest(
    @SerializedName("sourceId")
    val sourceId: Int = 0,
    @SerializedName("targetId")
    val targetId: Int = 0,
    @SerializedName("liked")
    val liked: Boolean = false
)
