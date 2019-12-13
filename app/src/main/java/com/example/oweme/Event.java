package com.example.oweme;

public class Event {

    String eid;
    String eventName;
    String status;
    String members;

    public Event() {
    }

    public Event(String eid, String eventName, String status, String members) {

        this.eid = eid;
        this.eventName = eventName;
        this.status = status;
        this.members = members;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

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

}
