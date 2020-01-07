package com.example.oweme;

import java.util.Date;

public class Expense {

    String eid;
    String description;
    Date date;
    double amount;
    String uid;
    String members;


    public Expense(String eid, String description, Date date, double amount, String uid, String members) {
        this.eid = eid;
        this.description = description;
        this.date = date;
        this.amount = amount;
        this.uid = uid;
        this.members = members;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }
}
