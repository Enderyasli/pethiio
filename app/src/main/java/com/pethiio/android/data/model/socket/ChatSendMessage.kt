package com.pethiio.android.data.model.socket

import com.google.gson.annotations.SerializedName

class ChatSendMessage(
    @SerializedName("content")
    val content: String,
    @SerializedName("roomId")
    val roomId: Int,
    @SerializedName("senderMemberId")
    val senderMemberId: Int
)
