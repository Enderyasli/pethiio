package com.pethiio.android.data.model

import com.google.gson.annotations.SerializedName

data class PethiioResponse(

    @SerializedName("key")
    val key: String = "",
    @SerializedName("value")
    val value: String = "",

)