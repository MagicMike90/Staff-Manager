package com.sanitation.app.staffmanagment.signhistory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Michael on 3/2/17.
 */
public class SignManager {
    private static final String TAG = "SignManager";
    private static SignManager mInstance;
    private List<SignHistory> mNotices;
    public static final Map<String, SignHistory> NOTICE_MAP = new ConcurrentHashMap<String, SignHistory>();

    public static synchronized SignManager getInstance() {
        if (mInstance == null)
            mInstance = new SignManager();

        return mInstance;
    }

    public void init() {
        mNotices = Collections.synchronizedList(new ArrayList<SignHistory>());
    }


    private SignManager() {
        init();
    }

    // retrieve array from anywhere
    public List<SignHistory> getNotice() {
        return this.mNotices;
    }

    public SignHistory getNotice(String id) {
        return NOTICE_MAP.get(id);
    }

    //Add element to array
    public void addNotice(SignHistory value) {
//        Log.d(TAG, "addNotice");
        NOTICE_MAP.put(value.id,value);
        mNotices.add(value);
    }
}
