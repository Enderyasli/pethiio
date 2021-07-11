package com.pethiio.android.data.model.notification

import com.google.gson.annotations.SerializedName

data class NotificationListResponse(

    @SerializedName("active")
    val active: Boolean = false,
    @SerializedName("type")
    val type: String = ""
)


