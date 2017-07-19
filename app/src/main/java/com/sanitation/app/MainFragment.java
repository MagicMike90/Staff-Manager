package com.sanitation.app;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sanitation.app.notice.Notice;
import com.sanitation.app.notice.NoticeManager;
import com.sanitation.app.factory.signhistory.SignHistory;
import com.sanitation.app.factory.signhistory.SignManager;
import com.sanitation.app.staff.Staff;
import com.sanitation.app.staff.StaffManager;
import com.sanitation.app.notice.NoticeListFragment;
import com.sanitation.app.staff.staffmanagement.signhistory.SignListFragment;
import com.sanitation.app.staff.StaffListFragment;
import com.ss.bottomnavigation.BottomNavigation;
import com.ss.bottomnavigation.events.OnSelectedItemChangeListener;

import org.json.JSONException;
import org.json.JSONObject;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.MeteorSingleton;


public class MainFragment extends Fragment implements MeteorCallback {
        private static final String TAG = "MainFragment";

    private Context mContext;
    private Utils mUtils;
    private Meteor mMeteor;

    private NoticeListFragment mNoticeListFragment;
    private StaffListFragment mStaffListFragment;
    private SignListFragment mSignListFragment;


    public static final int STAFF_FRAGMENT_RESULT = 1;
    public static final String STAFF_FILTER_NAME = "staff_name";
    public static final String STAFF_FILTER_DEPARTMENT = "department";
    public static final String STAFF_FILTER_TIME = "sign_time";
    public static final String STAFF_FILTER_LATE = "late";

    public MainFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        mContext = this.getContext();
        mUtils = Utils.getInstance(mContext);
        mMeteor = MeteorSingleton.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        BottomNavigation bottomNavigation = (BottomNavigation) view.findViewById(R.id.bottom_navigation);

        mNoticeListFragment = NoticeListFragment.newInstance();
        mStaffListFragment = StaffListFragment.newInstance();
        mSignListFragment = SignListFragment.newInstance();

        bottomNavigation.setOnSelectedItemChangeListener(new OnSelectedItemChangeListener() {
            @Override
            public void onSelectedItemChanged(int itemId) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (itemId) {
                    case R.id.tab_home:

                        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                        transaction.replace(R.id.child_fragment_container, mNoticeListFragment).commit();
                        break;
                    case R.id.tab_images:

                        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                        transaction.replace(R.id.child_fragment_container, mStaffListFragment).commit();
                        break;
                    case R.id.tab_camera:
                        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                        transaction.replace(R.id.child_fragment_container, mSignListFragment).commit();
                        break;
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMeteor.removeCallbacks();
        mMeteor.addCallback(this);
        mMeteor.reconnect();

        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        mMeteor.removeCallback(this);
        mMeteor.disconnect();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onConnect(boolean signedInAutomatically) {
        Log.d(TAG, "onConnect");
        mMeteor.subscribe(Constants.MongoCollection.NOTICE);
        mMeteor.subscribe(Constants.MongoCollection.STAFF);
        mMeteor.subscribe(Constants.MongoCollection.SIGN);

        NoticeManager.getInstance().init();
        StaffManager.getInstance().init();
        SignManager.getInstance().init();
    }

    @Override
    public void onDisconnect() {
        Log.d(TAG, "onDisconnect");
    }

    @Override
    public void onException(Exception e) {
        Log.d(TAG, "onException");
    }

    @Override
    public void onDataAdded(String collectionName, String documentID, String newValuesJson) {
        Log.d(TAG, "onDataAdded:" + collectionName + " : " + newValuesJson);
        Fragment fragment = getFragmentManager().findFragmentById(R.id.child_fragment_container);

        switch (collectionName) {
            case Constants.MongoCollection.NOTICE:
                try {
                    JSONObject obj = new JSONObject(newValuesJson);
                    String id = documentID;
                    String name = obj.getString("title");
                    String content = obj.getString("content");
                    String time = obj.getString("updateAt");
                    time = mUtils.getDateStr(time);

                    Log.d(TAG,name);

                    NoticeManager.getInstance().addNotice(new Notice(id, name, content, time));
                    if (fragment instanceof NoticeListFragment)
                        mNoticeListFragment.updateList(NoticeManager.getInstance().getNotices());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.MongoCollection.STAFF:
                JSONObject staff = null;
                try {
                    staff = new JSONObject(newValuesJson);
                    String staff_id = staff.has("staff_id") ? staff.getString("staff_id") : " ";
                    String name = staff.has("staff_name") ? staff.getString("staff_name") : " ";
                    String gender = staff.has("gender") ? staff.getString("gender") : " ";
                    String date = staff.has("join_work_date") ? staff.getString("join_work_date") : "0";

                    name = mUtils.getName(name);
                    gender = mUtils.getGender(gender);
                    date = mUtils.getDateStr(date);

                    StaffManager.getInstance().addStaffs(new Staff(documentID,staff_id, name, gender, date));
                    if (fragment instanceof StaffListFragment)
                        mStaffListFragment.updateList(StaffManager.getInstance().getStaffs());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.MongoCollection.SIGN:
                try {
                    JSONObject newVal = new JSONObject(newValuesJson);
                    String staff_name = newVal.has("staff_name") ? newVal.getString("staff_name").toString() : "null";
                    String staff_department = newVal.has("staff_department") ? newVal.getString("staff_department").toString() : "null";
                    String createdAt = newVal.has("createdAt") ? mUtils.getDateStr(newVal.getString("createdAt")) : "0";

                    SignManager.getInstance().addSign(new SignHistory(collectionName, staff_name, staff_department, createdAt));
                    if (fragment instanceof SignListFragment)
                        mSignListFragment.updateList(SignManager.getInstance().getSigns());
                } catch (JSONException e) {
                    Log.d(TAG, Log.getStackTraceString(e));
                }

                break;
        }
    }

    @Override
    public void onDataChanged(String collectionName, String documentID, String updatedValuesJson, String removedValuesJson) {
        Log.d(TAG, collectionName);
        switch (collectionName) {
            case Constants.MongoCollection.NOTICE:
                break;
            case Constants.MongoCollection.STAFF:
                break;
            case Constants.MongoCollection.SIGN:
                break;
        }

    }

    @Override
    public void onDataRemoved(String collectionName, String documentID) {
        Log.d(TAG, "onDataRemoved");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");
        switch (requestCode) {
            case STAFF_FRAGMENT_RESULT: {
                if (resultCode == Activity.RESULT_OK) {
                    String name = data.getStringExtra(STAFF_FILTER_NAME);
                    String department = data.getStringExtra(STAFF_FILTER_DEPARTMENT);
                    String time = data.getStringExtra(STAFF_FILTER_TIME);
                    String late = data.getStringExtra(STAFF_FILTER_LATE);
                    Fragment fragment = getFragmentManager().findFragmentById(R.id.child_fragment_container);

                    Log.d(TAG, "name: " + name);
                }
                break;
            }
        }
    }
}
