package com.javaboxchat.model;

public class FriendInfo {

    private String username;
    private boolean online;

    public FriendInfo(
            String username,
            boolean online
    ) {
        this.username = username;
        this.online = online;
    }

    public String getUsername() {
        return username;
    }

    public boolean isOnline() {
        return online;
    }
}