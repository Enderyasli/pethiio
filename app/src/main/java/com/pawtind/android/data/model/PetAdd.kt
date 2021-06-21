package com.pawtind.android.data.model

import com.google.gson.annotations.SerializedName
import com.pawtind.android.data.model.PawtindResponse

data class PetAdd(

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

