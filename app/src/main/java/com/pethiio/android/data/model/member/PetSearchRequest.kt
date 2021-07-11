package com.pethiio.android.data.model.member

import com.google.gson.annotations.SerializedName

data class PetSearchRequest(
    @SerializedName("sourceMemberId")
    val sourceId: Int = 0,
    @SerializedName("targetMemberId")
    val targetId: Int = 0,
    @SerializedName("liked")
    val liked: Boolean = false
)
