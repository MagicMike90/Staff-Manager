package com.sanitation.app.staff;

import android.util.Log;

import com.sanitation.app.notice.Notice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Michael on 3/2/17.
 */
public class StaffManager {
    private static final String TAG  = "NoticeManager";
    private static StaffManager mInstance;
    private List<Notice> mNotices;

    public static synchronized StaffManager getInstance() {
        if (mInstance == null)
            mInstance = new StaffManager();

        return mInstance;
    }
    public void init() {
        mNotices = Collections.synchronizedList(new ArrayList<Notice>());
    }


    private StaffManager() {
        init();
    }

    // retrieve array from anywhere
    public List<Notice> getNotices() {
        return this.mNotices;
    }

    //Add element to array
    public void addNotice(Notice value) {
        Log.d(TAG,"addNotice");
        mNotices.add(value);
    }
}
