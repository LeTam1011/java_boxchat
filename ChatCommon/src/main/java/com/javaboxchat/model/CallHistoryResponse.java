package com.javaboxchat.model;

import java.util.List;

public class CallHistoryResponse {

    private String type;

    private List<CallHistory> calls;

    public CallHistoryResponse(
            String type,
            List<CallHistory> calls
    ) {
        this.type = type;
        this.calls = calls;
    }

    public String getType() {
        return type;
    }

    public List<CallHistory> getCalls() {
        return calls;
    }
}