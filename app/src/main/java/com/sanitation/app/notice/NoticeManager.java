package com.sanitation.app.notice;

import android.util.Log;

import com.sanitation.app.dummy.DummyContent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Michael on 3/2/17.
 */
public class NoticeManager {
    private static final String TAG = "NoticeManager";
    private static NoticeManager mInstance;
    private List<Notice> mNotices;
    public static final Map<String, Notice> NOTICE_MAP = new ConcurrentHashMap<String, Notice>();

    public static synchronized NoticeManager getInstance() {
        if (mInstance == null)
            mInstance = new NoticeManager();

        return mInstance;
    }

    public void init() {
        mNotices = Collections.synchronizedList(new ArrayList<Notice>());
    }


    private NoticeManager() {
        init();
    }

    // retrieve array from anywhere
    public List<Notice> getNotice() {
        return this.mNotices;
    }

    public Notice getNotice(String id) {
        return NOTICE_MAP.get(id);
    }

    //Add element to array
    public void addNotice(Notice value) {
        Log.d(TAG, "addNotice");
        NOTICE_MAP.put(value.id,value);
        mNotices.add(value);
    }
}
