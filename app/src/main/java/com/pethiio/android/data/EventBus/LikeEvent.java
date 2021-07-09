package com.pethiio.android.data.EventBus;

public class LikeEvent {

    public Boolean getLike() {
        return like;
    }

    public void setLike(Boolean like) {
        this.like = like;
    }

    public LikeEvent(Boolean like) {
        this.like = like;
    }

    Boolean like;
}
