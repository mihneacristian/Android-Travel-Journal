package com.mihneacristian.traveljournal.Database;

public class Users {

    private String userName;
    private String userEmailAddress;

    public Users() {
    }

    public Users(String userName, String userEmailAddress) {
        this.userName = userName;
        this.userEmailAddress = userEmailAddress;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmailAddress() {
        return userEmailAddress;
    }

    public void setUserEmailAddress(String userEmailAddress) {
        this.userEmailAddress = userEmailAddress;
    }
}
