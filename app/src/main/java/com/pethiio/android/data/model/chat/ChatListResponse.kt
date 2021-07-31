package com.pethiio.android.data.model.chat

import com.google.gson.annotations.SerializedName

class ChatListResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("petId")
    val petId: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("lastMessage")
    val lastMessage: String,
    @SerializedName("lastMessageTime")
    val lastMessageTime: String,
    @SerializedName("memberId")
    val memberId: Int,
    @SerializedName("unSeenMessagesCount")
    val unSeenMessagesCount: Int

)
