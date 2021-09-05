package com.pethiio.android.data.EventBus;

public class MatchEvent {

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceAvatar() {
        return sourceAvatar;
    }

    public void setSourceAvatar(String sourceAvatar) {
        this.sourceAvatar = sourceAvatar;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getTargetAvatar() {
        return targetAvatar;
    }

    public void setTargetAvatar(String targetAvatar) {
        this.targetAvatar = targetAvatar;
    }

    public MatchEvent(String purpose, int memberId, int roomId, String sourceName, String sourceAvatar, String targetName, String targetAvatar) {
        this.purpose = purpose;
        this.memberId = memberId;
        this.roomId = roomId;
        this.sourceName = sourceName;
        this.sourceAvatar = sourceAvatar;
        this.targetName = targetName;
        this.targetAvatar = targetAvatar;
    }

    String purpose;
    int memberId;
    int roomId;
    String sourceName;
    String sourceAvatar;
    String targetName;
    String targetAvatar;
}
