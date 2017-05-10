package com.sanitation.app.factory.notice;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
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
    private Map<String, Notice> NOTICE_MAP = new ConcurrentHashMap<String, Notice>();

    public static synchronized NoticeManager getInstance() {
        if (mInstance == null)
            mInstance = new NoticeManager();

        return mInstance;
    }

    public void init() {
        mNotices = Collections.synchronizedList(new ArrayList<Notice>());
        NOTICE_MAP = new ConcurrentHashMap<String, Notice>();
    }


    private NoticeManager() {
        init();
    }

    // retrieve array from anywhere
    public List<Notice> getNotices() {
        return this.mNotices;
    }

    public Notice getNotice(String id) {
        return NOTICE_MAP.get(id);
    }

    //Add element to array
    public void addNotice(Notice value) {
//        Log.d(TAG, "addSign");
        if(!NOTICE_MAP.containsKey(value.id)) {
            Log.d(TAG, "put");
            NOTICE_MAP.put(value.id,value);
            mNotices.add(value);
        }
    }

}
