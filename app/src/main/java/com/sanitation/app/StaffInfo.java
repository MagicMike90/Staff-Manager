package com.sanitation.app;

/**
 * Created by Michael on 4/23/17.
 */

public class StaffInfo {
    private static StaffInfo mInstance;

    private StaffInfo() {

    }

    public static StaffInfo getInstance() {
        if (mInstance == null) {
            mInstance = new StaffInfo();
        }
        return mInstance;
    }

    @Override
    public String toString() {
        return "id: "+ id + " staff_name: "+ staff_name + " staff_id: "+ staff_id + " department： "+ department +" role:" + role;
    }

    public String id;
    public String staff_name;
    public String staff_id;
    public String department;
    public String  role;
}
