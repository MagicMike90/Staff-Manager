package com.sanitation.app.factory.eventtype;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Michael on 3/2/17.
 */
public class EventTypeManager {
    private static final String TAG = "EventTypeManager";
    private static EventTypeManager mInstance;
    private List<EventType> mEventTypes;
    public static final Map<String, EventType> EVENT_TYPE_MAP = new ConcurrentHashMap<String, EventType>();

    public static synchronized EventTypeManager getInstance() {
        if (mInstance == null)
            mInstance = new EventTypeManager();

        return mInstance;
    }

    public void init() {
        mEventTypes = Collections.synchronizedList(new ArrayList<EventType>());
    }


    private EventTypeManager() {
        init();
    }

    // retrieve array from anywhere
    public List<EventType> getEventTypes() {
        return this.mEventTypes;
    }

    public List<String> getEventTypeNames() {
        Log.d(TAG, "getEventTypeNames: " + mEventTypes.size());
        List<String> names = new ArrayList<>();
        for (int i = 0; i < mEventTypes.size(); i++) {
            names.add(mEventTypes.get(i).name);
        }
        return names;
    }


    public EventType getEventTypeById(String id) {
        return EVENT_TYPE_MAP.get(id);
    }

    //Add element to array
    public void addEventType(EventType value) {
        Log.d(TAG, "EventType");
        if (!EVENT_TYPE_MAP.containsKey(value.id)) {

            EVENT_TYPE_MAP.put(value.id, value);
            mEventTypes.add(value);
        }
    }


}
