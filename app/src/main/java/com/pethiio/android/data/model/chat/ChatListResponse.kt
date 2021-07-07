package com.pethiio.android.data.model.chat

import com.google.gson.annotations.SerializedName

class ChatListResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
)
