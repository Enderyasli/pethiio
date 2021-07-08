package com.pethiio.android.data.model.chat

import com.google.gson.annotations.SerializedName

class ChatRoomResponse(
    @SerializedName("content")
    val content: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("senderMemberId")
    val senderMemberId: Int,
    @SerializedName("time")
    val time: String,
)
