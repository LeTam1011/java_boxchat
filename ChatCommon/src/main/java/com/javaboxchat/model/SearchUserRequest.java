package com.javaboxchat.model;

public class SearchUserRequest {

    private String type;
    private String phone;

    public SearchUserRequest(
            String type,
            String phone
    ) {
        this.type = type;
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public String getPhone() {
        return phone;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}