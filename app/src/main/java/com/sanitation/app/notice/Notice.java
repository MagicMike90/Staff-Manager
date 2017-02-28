package com.sanitation.app.notice;

/**
 * Created by Michael on 2/27/17.
 */

public class Notice {
    public final String title;
    public final String content;
    public final String date;

    public Notice(String title, String content, String date) {
        this.title = title;
        this.content = content;
        this.date = date;
    }

    @Override
    public String toString() {
        return content;
    }
}
