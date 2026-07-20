package com.javaboxchat.model;

import java.util.List;

public class FriendsResponse {

    private String type;
    private List<FriendInfo> friends;

    public FriendsResponse(
            String type,
            List<FriendInfo> friends
    ) {
        this.type = type;
        this.friends = friends;
    }

    public String getType() {
        return type;
    }

    public List<FriendInfo> getFriends() {
        return friends;
    }
}