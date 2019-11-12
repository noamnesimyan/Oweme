package com.example.oweme;

public class User {

    String uid;
    String nickName;
    String urlPhoto;

    public User(String uid, String nickName, String urlPhoto) {

        this.uid = uid;
        this.nickName = nickName;
        this.urlPhoto = urlPhoto;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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
}
