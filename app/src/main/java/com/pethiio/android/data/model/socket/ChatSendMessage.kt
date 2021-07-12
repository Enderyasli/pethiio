package com.pethiio.android.data.model.socket

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ChatSendMessage (
    @SerializedName("content")
    val content: String,
    @SerializedName("roomId")
    val roomId: Int,
    @SerializedName("senderMemberId")
    val senderMemberId: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(content)
        parcel.writeInt(roomId)
        parcel.writeInt(senderMemberId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChatSendMessage> {
        override fun createFromParcel(parcel: Parcel): ChatSendMessage {
            return ChatSendMessage(parcel)
        }

        override fun newArray(size: Int): Array<ChatSendMessage?> {
            return arrayOfNulls(size)
        }
    }
}
