package com.example.oweme;

public class User {

    String userID;
    String nickName;
    String urlPhoto;
    String events;

    public User() {
    }

    public User(String userID, String nickName, String urlPhoto) {

        this.userID = userID;
        this.nickName = nickName;
        this.urlPhoto = urlPhoto;
        this.events = "";
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public String getEvents()
    {
        return this.events;
    }

    public void setEvents(String events)
    {
        this.events = events;
    }


}
