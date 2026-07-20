package com.javaboxchat.model;

public class FriendRequestMessage {

    private String type;
    private String sender;
    private String receiver;

    public FriendRequestMessage(
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
}