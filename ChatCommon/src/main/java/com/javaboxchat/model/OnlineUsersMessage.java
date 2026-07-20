package com.javaboxchat.model;

import java.util.List;

public class OnlineUsersMessage {

    private String type;
    private List<String> users;

    public OnlineUsersMessage(
            String type,
            List<String> users
    ) {
        this.type = type;
        this.users = users;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}