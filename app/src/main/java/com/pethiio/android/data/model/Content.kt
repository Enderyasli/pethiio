package com.pethiio.android.data.model

import com.google.gson.annotations.SerializedName
import com.pethiio.android.data.model.member.PetSearchResponse

data class Content(
    @SerializedName("content")
    val content: List<PetSearchResponse> = emptyList()
)

