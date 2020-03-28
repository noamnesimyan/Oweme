package com.example.oweme;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "myExpenses")
public class Expense {

    @Ignore
    String description;
    @Ignore
    long CreatedDate;
    @Ignore
    double amount;
    @Ignore
    String owner;
    @Ignore
    String members;
    @Ignore
    String urlPhoto;

    @PrimaryKey
    @NonNull
    String expenseID;
    @NonNull
    String eventID; //The event that the expense belongs to


    public Expense(){}

    public Expense(String description, double amount, String owner, String members, String expenseID, String urlPhoto) {

        this.description = description;
        this.CreatedDate = System.currentTimeMillis();
        this.amount = amount;
        this.owner = owner;
        this.members = members;
        this.expenseID = expenseID;
        this.urlPhoto = urlPhoto;
    }

    public long getCreatedDate() { return CreatedDate; }

    public void setCreatedDate(long createdDate) { CreatedDate = createdDate; }

    public String getUrlPhoto() { return urlPhoto; }

    public void setUrlPhoto(String urlPhoto) { this.urlPhoto = urlPhoto; }

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
    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getExpenseID() {
        return expenseID;
    }

    public void setExpenseID(String expenseID) {
        this.expenseID = expenseID;
    }
}
