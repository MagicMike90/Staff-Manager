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

    public String staff_name;
    public String staff_id;
    public String staff_department;
    public String  staff_role;
}
