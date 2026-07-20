package com.javaboxchat.model;

public class UploadResponse {

    private boolean success;

    private String filePath;

    private String message;

    public UploadResponse() {
    }

    public UploadResponse(
            boolean success,
            String filePath,
            String message
    ) {
        this.success = success;
        this.filePath = filePath;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}