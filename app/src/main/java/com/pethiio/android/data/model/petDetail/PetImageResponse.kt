package com.pethiio.android.data.model.petDetail

import com.google.gson.annotations.SerializedName
import com.pethiio.android.data.model.PethiioResponse

class PetImageResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("indexOrder")
    val indexOrder: Int,
    @SerializedName("path")
    val path: String,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("petId")
    val petId: Int

)

