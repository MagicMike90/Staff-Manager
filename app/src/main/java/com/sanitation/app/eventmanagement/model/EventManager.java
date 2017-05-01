package com.sanitation.app.eventmanagement.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Michael on 3/2/17.
 */
public class EventManager {
    private static final String TAG = "SignManager";
    private static EventManager mInstance;
    private List<Event> mEvents;
    public static final Map<String, Event> EVENT_MAP = new ConcurrentHashMap<String, Event>();

    public static synchronized EventManager getInstance() {
        if (mInstance == null)
            mInstance = new EventManager();

        return mInstance;
    }

    public void init() {
        mEvents = Collections.synchronizedList(new ArrayList<Event>());
    }


    private EventManager() {
        init();
    }

    // retrieve array from anywhere
    public List<Event> getEvents() {
        return this.mEvents;
    }

    public Event getEvents(String id) {
        return EVENT_MAP.get(id);
    }

    //Add element to array
    public void addNotice(Event value) {
//        Log.d(TAG, "addSign");
        EVENT_MAP.put(value.id,value);
        mEvents.add(value);
    }
}
