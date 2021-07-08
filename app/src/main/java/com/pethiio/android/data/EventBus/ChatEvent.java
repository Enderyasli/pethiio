package com.pethiio.android.data.EventBus;

public class ChatEvent {
    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSenderMemberId() {
        return senderMemberId;
    }

    public void setSenderMemberId(int senderMemberId) {
        this.senderMemberId = senderMemberId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ChatEvent(int roomId, int id, String content, int senderMemberId, String time) {
        this.roomId = roomId;
        this.id = id;
        this.content = content;
        this.senderMemberId = senderMemberId;
        this.time = time;
    }

    int roomId;
    int id;
    String content;
    int senderMemberId;
    String time;
}
