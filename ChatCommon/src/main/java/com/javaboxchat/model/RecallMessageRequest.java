package com.javaboxchat.model;

public class RecallMessageRequest {

    private String type;
    private int messageId;

    public RecallMessageRequest(
            String type,
            int messageId
    ) {
        this.type = type;
        this.messageId = messageId;
    }

    public String getType() {
        return type;
    }

    public int getMessageId() {
        return messageId;
    }
}