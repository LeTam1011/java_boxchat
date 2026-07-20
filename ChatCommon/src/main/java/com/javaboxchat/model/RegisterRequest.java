package com.javaboxchat.model;

public class RegisterRequest {
    private String type;
    private String username;
    private String password;
    private String phone;

    public RegisterRequest(
            String type,
            String username,
            String password,
            String phone
    ) {
        this.type = type;
        this.username = username;
        this.password = password;
        this.phone = phone;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
