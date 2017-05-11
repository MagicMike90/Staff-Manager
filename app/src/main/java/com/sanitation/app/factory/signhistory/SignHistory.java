package com.sanitation.app.factory.signhistory;

/**
 * Created by Michael on 2/27/17.
 */

public class SignHistory {
    public final String id;
    public final String staff_name;
    public final String department;
//    public final String content;
    public final String date;

    public SignHistory(String id, String title, String department, String date) {
        this.id = id;
        this.staff_name = title;
        this.department = department;
//        this.content = content;
        this.date = date;
    }

    @Override
    public String toString() {
        return department;
    }
}
