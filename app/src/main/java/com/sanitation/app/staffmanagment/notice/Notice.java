package com.sanitation.app.staffmanagment.notice;

/**
 * Created by Michael on 2/27/17.
 */

public class Notice {
    public final String id;
    public final String title;
    public final String content;
    public final String date;

    public Notice(String id,String title, String content, String date) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
    }

    @Override
    public String toString() {
        return content;
    }
}
