package com.sanitation.app.StaffManagement.models;

public class Staff {

    public final String id;
    public final String staff_id;
    public final String staff_name;
    public final String gender;
    public final String join_work_date;


    public Staff(String id, String staff_id,String _staff_name, String _gender, String _join_work_date) {
        this.id = id;
        this.staff_id = staff_id;
        this.staff_name = _staff_name;
        this.gender = _gender;
        this.join_work_date = _join_work_date;
    }

    @Override
    public String toString() {
        return staff_name;
    }
}
