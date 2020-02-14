package com.example.oweme;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "MyDepths")
public class Depth {

    @PrimaryKey
    @NonNull
    private double amount;
    @NonNull
    private String userID;

    public Depth(String userID, double amount) {
        this.userID = userID;
        this.amount = amount;
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
}