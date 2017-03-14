package com.sanitation.app.staff;

import android.util.Log;

import com.sanitation.app.notice.Notice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Michael on 3/2/17.
 */
public class StaffManager {
    private static final String TAG = "StaffManager";
    private static StaffManager mInstance;
    private List<Staff> mStaffs;
    public static final Map<String, Staff> STAFF_MAP = new ConcurrentHashMap<String, Staff>();

    public static synchronized StaffManager getInstance() {
        if (mInstance == null)
            mInstance = new StaffManager();

        return mInstance;
    }

    public void init() {
        mStaffs = Collections.synchronizedList(new ArrayList<Staff>());
    }


    private StaffManager() {
        init();
    }

    // retrieve array from anywhere
    public List<Staff> getStaffs() {
        return this.mStaffs;
    }

    public Staff getStaff(String id) {
        return STAFF_MAP.get(id);
    }

    //Add element to array
    public void addStaffs(Staff value) {
        STAFF_MAP.put(value.id,value);
        mStaffs.add(value);
    }
}
