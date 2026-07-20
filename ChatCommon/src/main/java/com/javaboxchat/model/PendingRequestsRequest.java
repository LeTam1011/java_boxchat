package com.javaboxchat.model;

public class PendingRequestsRequest {

    private String type;
    private String username;

    public PendingRequestsRequest() {
    }

    public PendingRequestsRequest(
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