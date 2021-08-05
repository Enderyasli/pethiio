package com.pethiio.android.data.model.vet

import com.google.gson.annotations.SerializedName

class VetResponse(
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("detail")
    val detail: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("title")
    val title: String
)
