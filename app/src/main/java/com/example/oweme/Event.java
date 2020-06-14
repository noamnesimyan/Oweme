package com.example.oweme;

import java.util.ArrayList;

public class Event {

    String eventID;
    String eventName;
    String status;
    String members;
    long createdDate;
    ArrayList<Expense> expenses;

    public Event() {
    }

    public Event(String eventID, String eventName, String status, String members) {

        this.eventID = eventID;
        this.eventName = eventName;
        this.status = status;
        this.members = members;
        this.createdDate = System.currentTimeMillis();
        expenses = new ArrayList<Expense>();
    }

    public long getCreatedDate() { return createdDate; }

    public void setCreatedDate(long createdDate) { this.createdDate = createdDate; }

    public void setMembers(String members) { this.members = members; }

    public ArrayList<Expense> getExpenses() { return expenses; }

    public void setExpenses(ArrayList<Expense> expenses) { this.expenses = expenses; }

    public String getEVentID() {
        return eventID;
    }

    public void setEventID(String eventID) { this.eventID = eventID; }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMembers()
    {
        return this.members;
    }

    public void addNewExpense(Expense expense) { this.expenses.add(expense);}

    public void removeExpense(Expense expense) { this.expenses.remove(expense);}

}
