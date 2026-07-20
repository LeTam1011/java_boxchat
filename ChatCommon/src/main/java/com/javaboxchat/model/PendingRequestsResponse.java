package com.javaboxchat.model;

import java.util.List;

public class PendingRequestsResponse {

    private String type;
    private List<String> requests;

    public PendingRequestsResponse(
            String type,
            List<String> requests
    ) {
        this.type = type;
        this.requests = requests;
    }

    public String getType() {
        return type;
    }

    public List<String> getRequests() {
        return requests;
    }
}