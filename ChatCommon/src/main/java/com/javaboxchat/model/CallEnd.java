package com.javaboxchat.model;

public class CallEnd {

    private String type;
    private String caller;
    private String receiver;

    public CallEnd() {
    }

    public CallEnd(
            String type,
            String caller,
            String receiver
    ) {
        this.type = type;
        this.caller = caller;
        this.receiver = receiver;
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
}