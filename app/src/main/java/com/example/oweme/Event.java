package com.example.oweme;

import java.util.ArrayList;
import java.util.Date;

public class Event {

    String eid;
    String eventName;
    String status;
    String members;
    ArrayList<Expense> expenses;

    public Event() {
    }

    public Event(String eid, String eventName, String status, String members) {

        this.eid = eid;
        this.eventName = eventName;
        this.status = status;
        this.members = members;
        expenses = new ArrayList<Expense>();
        Date date = new Date();
        expenses.add(new Expense("ff", "pipikaki",date , 20, "hhh", "bar"));
    }

    public void setMembers(String members) { this.members = members; }

    public ArrayList<Expense> getExpenses() { return expenses; }

    public void setExpenses(ArrayList<Expense> expenses) { this.expenses = expenses; }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) { this.eid = eid; }

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

    public void addnewExpense(Expense expense) { this.expenses.add(expense);}

    public void removeExpense(Expense expense) { this.expenses.remove(expense);}

}
