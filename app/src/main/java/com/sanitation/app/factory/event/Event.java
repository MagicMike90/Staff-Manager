package com.sanitation.app.factory.event;

/**
 * Created by Michael on 2/27/17.
 */

public class Event {
    public final String id;

    public final String type;
    public final String status;
    public final String description;

    public final String upload_time;
    public final String duration;


    public Event(String id, String type, String status, String description, String upload_time, String duration) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.status = status;

        this.upload_time = upload_time;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return description;
    }
}
