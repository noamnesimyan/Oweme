package com.example.oweme;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "MyDebts")
public class Debt {

    @PrimaryKey
    @NonNull
    private String userID;

    @NonNull
    private double amount;

    String nickName;

    String urlPhoto;

    boolean permanent;

    public Debt(String userID, double amount, boolean permanent) {
        this.userID = userID;
        this.amount = amount;
        this.permanent = permanent;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
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

    public boolean isPermanent() { return permanent; }

    public void setPermanent(boolean permanent) { this.permanent = permanent; }
}
