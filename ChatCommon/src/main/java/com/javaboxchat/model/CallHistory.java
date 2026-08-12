package com.javaboxchat.model;

public class CallHistory {

    private int id;

    private String caller;

    private String receiver;

    private String startTime;

    private String endTime;

    private int durationSeconds;

    private String status;


    public CallHistory(
            int id,
            String caller,
            String receiver,
            String startTime,
            String endTime,
            int durationSeconds,
            String status
    ) {

        this.id = id;
        this.caller = caller;
        this.receiver = receiver;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationSeconds = durationSeconds;
        this.status = status;
    }


    public int getId() {

        return id;
    }


    public String getCaller() {

        return caller;
    }


    public String getReceiver() {

        return receiver;
    }


    public String getStartTime() {

        return startTime;
    }


    public String getEndTime() {

        return endTime;
    }


    public int getDurationSeconds() {

        return durationSeconds;
    }


    public String getStatus() {

        return status;
    }
}