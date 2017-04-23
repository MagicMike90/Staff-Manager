package com.sanitation.app.staffsignin.step;

import android.content.Context;

import com.sanitation.app.VolleySingleton;

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

    public String department;
    public String name;
}
