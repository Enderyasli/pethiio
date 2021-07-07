package com.pethiio.android.data.model

import com.google.gson.annotations.SerializedName

data class PetEdit(

    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("about")
    val about: String = "",
    @SerializedName("animalId")
    val animalId: Int = 0,
    @SerializedName("animalPersonalities")
    val animalPersonalities: ArrayList<Int>,
    @SerializedName("breedId")
    val breedId: Int = 0,
    @SerializedName("color")
    val color: String = "",
    @SerializedName("gender")
    val gender: String = "",
    @SerializedName("month")
    val month: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("purpose")
    val purpose: String = "",
    @SerializedName("year")
    val year: Int = 0


)

