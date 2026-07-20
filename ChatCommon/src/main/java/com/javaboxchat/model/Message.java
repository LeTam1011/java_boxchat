package com.javaboxchat.model;

public class Message {

    private int id;
    private String type;
    private String sender;
    private String receiver;
    private String content;
    private String timestamp;
    private boolean recalled;
    private String messageType;
    private String filePath;

    public Message(
            String type,
            String sender,
            String receiver,
            String content,
            String timestamp,
            String messageType
    ) {
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.timestamp = timestamp;
        this.messageType = messageType;
    }

    public int getId() {
        return id;
    }

    public void setId(
            int id
    ) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(
            String timestamp
    ) {
        this.timestamp = timestamp;
    }
    public boolean isRecalled() {
        return recalled;
    }
    public void setRecalled(
            boolean recalled
    ) {
        this.recalled = recalled;
    }
    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(
            String messageType
    ) {
        this.messageType = messageType;
    }
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}