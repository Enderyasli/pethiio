package com.pethiio.android.data.model

import com.google.gson.annotations.SerializedName

data class AnimalDetailResponse(

    @SerializedName("breeds")
    val breeds: List<PawtindResponse>,
    @SerializedName("personalities")
    val personalities: List<PawtindResponse>


)