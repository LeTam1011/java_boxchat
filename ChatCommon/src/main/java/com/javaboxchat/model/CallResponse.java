package com.javaboxchat.model;

public class CallResponse {

    private String type;
    private String caller;
    private String receiver;
    private boolean accepted;

    public CallResponse() {
    }

    public CallResponse(
            String type,
            String caller,
            String receiver,
            boolean accepted
    ) {
        this.type = type;
        this.caller = caller;
        this.receiver = receiver;
        this.accepted = accepted;
    }

    public String getType() {
        return type;
    }

    public void setType(
            String type
    ) {
        this.type = type;
    }

    public String getCaller() {
        return caller;
    }

    public void setCaller(
            String caller
    ) {
        this.caller = caller;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(
            String receiver
    ) {
        this.receiver = receiver;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(
            boolean accepted
    ) {
        this.accepted = accepted;
    }
}