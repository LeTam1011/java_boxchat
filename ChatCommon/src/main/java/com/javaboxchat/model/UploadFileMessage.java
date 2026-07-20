package com.javaboxchat.model;

public class UploadFileMessage {

    private String type;
    private String sender;
    private String receiver;
    private String fileName;
    private String filePath;

    public UploadFileMessage(
            String type,
            String sender,
            String receiver,
            String fileName,
            String filePath
    ) {

        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
        this.fileName = fileName;
        this.filePath = filePath;
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

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(
            String filePath
    ) {
        this.filePath = filePath;
    }
}