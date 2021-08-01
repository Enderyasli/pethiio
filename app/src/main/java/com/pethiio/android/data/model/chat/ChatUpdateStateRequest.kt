package com.pethiio.android.data.model.chat

import com.google.gson.annotations.SerializedName

class ChatUpdateStateRequest(
    @SerializedName("lastMessageId")
    val lastMessageId: Int,
    @SerializedName("roomId")
    val roomId: Int,
    @SerializedName("senderMemberId")
    val senderMemberId: Int,
    @SerializedName("state")
    val state: String = "SEEN"
)
