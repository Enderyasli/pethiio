package com.pethiio.android.data.model.settings

import com.google.gson.annotations.SerializedName

data class FAQResponse(

    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("question")
    val question: String = "",
    @SerializedName("answer")
    val answer: String = "",
    var isExpanded: Boolean = false,

    )