package com.javaboxchat.model;

public class SearchUserResponse {

    private String type;
    private boolean found;
    private String username;

    public SearchUserResponse(
            String type,
            boolean found,
            String username
    ) {
        this.type = type;
        this.found = found;
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public boolean isFound() {
        return found;
    }

    public String getUsername() {
        return username;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}