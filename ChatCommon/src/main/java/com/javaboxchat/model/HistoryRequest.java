package com.javaboxchat.model;

public class HistoryRequest {

    private String type;
    private String sender;
    private String receiver;

    public HistoryRequest(
            String type,
            String sender,
            String receiver
    ) {
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
    }

    public String getType() {
        return type;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}