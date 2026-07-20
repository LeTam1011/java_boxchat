package com.javaboxchat.model;

public class User {

    private String username;
    private String phone;
    private String status;
    private boolean blocked;
    private String avatar;

    public User(
            String username,
            String phone,
            String status,
            boolean blocked,
            String avatar
    ) {
        this.username = username;
        this.phone = phone;
        this.status = status;
        this.blocked = blocked;
    }

    public String getUsername() {
        return username;
    }

    public String getPhone() {
        return phone;
    }

    public String getStatus() {
        return status;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public String getAvatar() {
        return avatar;
    }
}