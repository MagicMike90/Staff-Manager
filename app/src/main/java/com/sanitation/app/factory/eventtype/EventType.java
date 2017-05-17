package com.sanitation.app.factory.eventtype;

/**
 * Created by Michael on 2/27/17.
 */

public class EventType {
    public final String id;
    public final String name;
    public final String description;
    public final String duration;


    public EventType(String id, String name, String description, String duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duration = duration;

    }

    @Override
    public String toString() {
        return description;
    }
}
