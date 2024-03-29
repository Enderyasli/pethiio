package com.pethiio.android.data.model.member

import com.google.gson.annotations.SerializedName

data class LocationsRequest(
    @SerializedName("latitude")
    val latitude: String = "",
    @SerializedName("longitude")
    val longitude: String = ""
)
