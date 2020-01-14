package com.example.oweme;

public class Expense {

    String eid;
    String description;
    long CreatedDate;
    double amount;
    String owner;
    String members;


    public Expense(String eid, String description, double amount, String owner, String members) {

        this.eid = eid;
        this.description = description;
        this.CreatedDate = System.currentTimeMillis();
        this.amount = amount;
        this.owner = owner;
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
