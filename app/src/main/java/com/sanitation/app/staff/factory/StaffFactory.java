package com.sanitation.app.staff.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class StaffFactory implements MeteorCallback {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Staff> STAFFS = new ArrayList<Staff>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Staff> STAFF_MAP = new HashMap<String, Staff>();

    private static final String[] mUserDataAttrs = {"staff_name", "gender", "join_work_date"};


    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createStaff(i));
        }
    }

    private static void addItem(Staff staff) {
        STAFFS.add(staff);
        STAFF_MAP.put(staff.id, staff);
    }

    private static Staff createStaff(int position) {
        return new Staff(String.valueOf(position), "Item " + position, makeDetails(position), "1000");
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    @Override
    public void onConnect(boolean signedInAutomatically) {

    }

    @Override
    public void onDisconnect() {

    }

    @Override
    public void onException(Exception e) {

    }

    @Override
    public void onDataAdded(String collectionName, String documentID, String newValuesJson) {

    }

    @Override
    public void onDataChanged(String collectionName, String documentID, String updatedValuesJson, String removedValuesJson) {

    }

    @Override
    public void onDataRemoved(String collectionName, String documentID) {

    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class Staff {
        public final String id;
        public final String staff_name;
        public final String gender;
        public final String join_work_date;


        public Staff(String id, String _staff_name, String _gender, String _join_work_date) {
            this.id = id;
            this.staff_name = _staff_name;
            this.gender = _gender;
            this.join_work_date = _join_work_date;
        }

        @Override
        public String toString() {
            return gender;
        }
    }
}
