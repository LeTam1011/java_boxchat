package com.javaboxchat.model;

public class GetFriendsRequest {

    private String type;
    private String username;

    public GetFriendsRequest(
            String type,
            String username
    ) {
        this.type = type;
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }
}