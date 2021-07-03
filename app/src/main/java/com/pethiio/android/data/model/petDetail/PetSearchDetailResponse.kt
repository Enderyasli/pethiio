package com.pethiio.android.data.model.petDetail

import com.google.gson.annotations.SerializedName
import com.pethiio.android.data.model.PethiioResponse

class PetSearchDetailResponse(
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("memberId")
    val memberId: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("breed")
    val breed: String,
    @SerializedName("purpose")
    val purpose: String,
    @SerializedName("age")
    val age: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("color")
    val color: String,
    @SerializedName("about")
    val about: String,
    @SerializedName("distance")
    val distance: Int,
    @SerializedName("photos")
    val photos: List<String>,
    @SerializedName("personalities")
    val personalities: List<String>

)

