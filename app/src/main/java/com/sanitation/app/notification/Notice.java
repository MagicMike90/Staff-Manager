package com.sanitation.app.notification;

/**
 * Created by Michael on 2/27/17.
 */

public class Notice {
    public final String id;
    public final String title;
    public final String content;


    public Notice(String id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    @Override
    public String toString() {
        return content;
    }
}
