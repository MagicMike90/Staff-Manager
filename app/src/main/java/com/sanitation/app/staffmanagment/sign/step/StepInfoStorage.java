package com.sanitation.app.staffmanagment.sign.step;

/**
 * Created by Michael on 4/23/17.
 */

public class StepInfoStorage {
    private static StepInfoStorage mInstance;

    private StepInfoStorage() {

    }

    public static StepInfoStorage getInstance() {
        if (mInstance == null) {
            mInstance = new StepInfoStorage();
        }
        return mInstance;
    }

    public int sign_type;
    public String staff_department;
    public String staff_name;
    public String staff_role;
    public String type;
    public double latitude;
    public double longitude;
}
