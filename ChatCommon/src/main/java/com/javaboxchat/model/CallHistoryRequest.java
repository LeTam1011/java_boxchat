package com.javaboxchat.model;

public class CallHistoryRequest {

    private String type;

    private String username;

    public CallHistoryRequest(
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