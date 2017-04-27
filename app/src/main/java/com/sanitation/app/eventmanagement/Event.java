package com.sanitation.app.eventmanagement;

/**
 * Created by Michael on 2/27/17.
 */

public class Event {
    public final String id;
    public final String title;
    public final String content;
    public final String date;
    public final String status;

    public Event(String id, String title, String content, String date, String status) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.status = status;
    }

    @Override
    public String toString() {
        return content;
    }
}
