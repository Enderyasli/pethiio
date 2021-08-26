package com.pethiio.android.data.model.filter

import com.google.gson.annotations.SerializedName

data class SearchFilterRequest(
    @SerializedName("animalId")
    val animalId: Int,
    @SerializedName("breedId")
    val breedId: Int,
    @SerializedName("purpose")
    val purpose: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("minimumAge")
    val minimumAge: Int,
    @SerializedName("maximumAge")
    val maximumAge: Int,
    @SerializedName("minimumDistance")
    val minimumDistance: Int,
    @SerializedName("maximumDistance")
    val maximumDistance: Int,
)