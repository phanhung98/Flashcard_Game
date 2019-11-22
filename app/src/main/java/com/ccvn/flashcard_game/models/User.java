package com.ccvn.flashcard_game.models;

public class User {
    private String username;
    private String userpassword;
    private String usersalt;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    public String getUsersalt() {
        return usersalt;
    }

    public void setUsersalt(String usersalt) {
        this.usersalt = usersalt;
    }
}
