package com.javaboxchat.model;

public class CallConnectInfo {

    private String type;

    private String partner;

    private String ip;

    private int port;

    public CallConnectInfo() {
    }

    public CallConnectInfo(
            String type,
            String partner,
            String ip,
            int port
    ) {
        this.type = type;
        this.partner = partner;
        this.ip = ip;
        this.port = port;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}