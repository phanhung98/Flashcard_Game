package com.example.flashcard_game.Models;

public class Users {
    private String Username;
    private String Userpassword;
    private String Usersalt;

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getUserpassword() {
        return Userpassword;
    }

    public void setUserpassword(String userpassword) {
        Userpassword = userpassword;
    }

    public String getUsersalt() {
        return Usersalt;
    }

    public void setUsersalt(String usersalt) {
        Usersalt = usersalt;
    }
}
