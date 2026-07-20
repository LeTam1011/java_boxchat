package com.javaboxchat.model;

public class LoginRequest {
    private String type;
    private String username;
    private String password;

    public LoginRequest(
            String type,
            String username,
            String password
    ) {
        this.type = type;
        this.username = username;
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
