package com.pethiio.android.data.model.member

import com.google.gson.annotations.SerializedName

data class PetSearchResponse(
    @SerializedName("petId")
    val petId: Int = 0,

    @SerializedName("memberId")
    val memberId: Int = 0,

    @SerializedName("ownerId")
    val ownerId: Int = 0,

    @SerializedName("ownerAvatar")
    val ownerAvatar: String = "",

    @SerializedName("owner")
    val owner: String = "",

    @SerializedName("avatar")
    val avatar: String = "",

    @SerializedName("name")
    val name: String = "",

    @SerializedName("age")
    val age: Int = 0,

    @SerializedName("breed")
    val breed: String = ""
)