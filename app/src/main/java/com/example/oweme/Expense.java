package com.example.oweme;

import android.net.Uri;

public class Expense {

    String description;
    long CreatedDate;
    double amount;
    String owner;
    String members;
    Uri picture;
    String eid;//expense id


    public Expense(){}

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public Expense(String description, double amount, String owner, String members, String eid) {

        this.description = description;
        this.CreatedDate = System.currentTimeMillis();
        this.amount = amount;
        this.owner = owner;
        this.members = members;
        this.eid = eid;
       // this.picture = picture; להוסיף תמונה!!!!
    }

    public long getCreatedDate() { return CreatedDate; }

    public void setCreatedDate(long createdDate) { CreatedDate = createdDate; }

    public Uri getPicture() { return picture; }

    public void setPicture(Uri picture) { this.picture = picture; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDate() {
        return CreatedDate;
    }

    public void setDate(long CreatedDate) {
        this.CreatedDate = CreatedDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }
}
