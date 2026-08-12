package com.javaboxchat.model;

public class CallEnd {

    private String type;

    private String caller;

    private String receiver;

    private int durationSeconds;


    public CallEnd(
            String type,
            String caller,
            String receiver,
            int durationSeconds
    ) {

        this.type = type;

        this.caller = caller;

        this.receiver = receiver;

        this.durationSeconds =
                durationSeconds;
    }


    public String getType() {

        return type;
    }


    public String getCaller() {

        return caller;
    }


    public String getReceiver() {

        return receiver;
    }


    public int getDurationSeconds() {

        return durationSeconds;
    }
}