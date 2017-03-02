package com.sanitation.app.notice;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Michael on 3/2/17.
 */
public class NoticeManager {
    private static final String TAG  = "NoticeManager";
    private static NoticeManager mInstance;
    private List<Notice> mNotices;

    public static synchronized NoticeManager getInstance() {
        if (mInstance == null)
            mInstance = new NoticeManager();

        return mInstance;
    }


    private NoticeManager() {
        mNotices = Collections.synchronizedList(new ArrayList<Notice>());
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
