package com.javaboxchat.model;

import java.util.List;

public class HistoryResponse {

    private String type;
    private List<Message> messages;

    public HistoryResponse(
            String type,
            List<Message> messages
    ) {
        this.type = type;
        this.messages = messages;
    }

    public String getType() {
        return type;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}