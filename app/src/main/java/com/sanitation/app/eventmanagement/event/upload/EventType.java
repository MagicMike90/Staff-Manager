package com.sanitation.app.eventmanagement.event.upload;

/**
 * Created by Michael on 5/8/17.
 */

public class EventType {
    public EventType(String name, String description,String duration) {
        this.name = name;
        this.description = description;
        this.duration = duration;
    }
    public String name;
    public String description;
    public String duration;
}
