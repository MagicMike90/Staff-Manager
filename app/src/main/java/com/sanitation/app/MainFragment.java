package com.sanitation.app;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sanitation.app.factory.notice.Notice;
import com.sanitation.app.factory.notice.NoticeManager;
import com.sanitation.app.staffmanagement.notice.NoticeListFragment;
import com.sanitation.app.staffmanagement.signhistory.SignHistory;
import com.sanitation.app.staffmanagement.signhistory.SignListFragment;
import com.sanitation.app.staffmanagement.staff.Staff;
import com.sanitation.app.staffmanagement.staff.StaffListFragment;
import com.ss.bottomnavigation.BottomNavigation;
import com.ss.bottomnavigation.events.OnSelectedItemChangeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.MeteorSingleton;
import im.delight.android.ddp.ResultListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements MeteorCallback {
    private static final String TAG = "MainFragment";

    private Context mContext;
    private Utils mUtils;
    private Meteor mMeteor;

    private NoticeListFragment mNoticeListFragment;
//    private List<Notice> mNotices;

    private StaffListFragment mStaffListFragment;
    private List<Staff> mStaffs;

    private SignListFragment mSignListFragment;
    private List<SignHistory> mSigns;

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

        bottomNavigation.setOnSelectedItemChangeListener(new OnSelectedItemChangeListener() {
            @Override
            public void onSelectedItemChanged(int itemId) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                Class fragmentClass = NoticeListFragment.class;
//                Fragment fragment = null;
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                Object[] queryParams = {};
                switch (itemId) {
                    case R.id.tab_home:
//                        fragmentClass = NoticeListFragment.class;
                        mNoticeListFragment = NoticeListFragment.newInstance();
                        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                        transaction.replace(R.id.child_fragment_container, mNoticeListFragment).commit();


//                        mMeteor.call("notices.getAll", queryParams, new ResultListener() {
//                            @Override
//                            public void onSuccess(String result) {
//                                Log.d(TAG, "Call result: " + result);
//                                try {
//                                    JSONArray res = new JSONArray(result);
//                                    for (int i = 0; i < res.length(); i++) {
//                                        JSONObject obj = res.getJSONObject(i);
//                                        String id = obj.getString("_id");
//                                        String name = obj.getString("title");
//                                        String content = obj.getString("content");
//                                        String time = obj.getString("updateAt");
//                                        time = mUtils.getDateStr(time);
//                                        mNotices.add(new Notice(id, name, content, time));
//                                    }
//                                    mNoticeListFragment.updateList(mNotices);
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                            @Override
//                            public void onError(String error, String reason, String details) {
//                                Log.d(TAG, "Error: " + error + " " + reason + " " + details);
//                            }
//                        });
                        break;
                    case R.id.tab_images:
//                        fragmentClass = StaffListFragment.class;
                        mStaffListFragment = mStaffListFragment.newInstance();

                        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                        transaction.replace(R.id.child_fragment_container, mStaffListFragment).commit();


                        mStaffs = new ArrayList<>();
                        mMeteor.call("staffs.getAll", queryParams, new ResultListener() {
                            @Override
                            public void onSuccess(String result) {
                                Log.d(TAG, "Call result: " + result);
                                try {
                                    JSONArray res = new JSONArray(result);
                                    for (int i = 0; i < res.length(); i++) {
                                        JSONObject obj = res.getJSONObject(i);
                                        String id = obj.getString("_id");


                                        String staff_name = obj.has("staff_name") ? obj.getString("staff_name").toString() : "null";
                                        String gender = obj.has("gender") ? obj.getString("gender").toString() : "null";
                                        String join_work_date = obj.has("join_work_date") ? obj.getString("join_work_date") : "0";


                                        staff_name = mUtils.getName(staff_name);
                                        gender = mUtils.getGender(gender);
                                        join_work_date = mUtils.getDateStr(join_work_date);

                                        mStaffs.add(new Staff(id, staff_name, gender, join_work_date));
                                    }
                                    mStaffListFragment.updateList(mStaffs);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(String error, String reason, String details) {
                                Log.d(TAG, "Error: " + error + " " + reason + " " + details);
                            }
                        });

                        break;
                    case R.id.tab_camera:
//                        fragmentClass = SignListFragment.class;
                        break;
                }
//                try {
//                    fragment = (Fragment) fragmentClass.newInstance();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                FragmentTransaction transaction = fragmentManager.beginTransaction();
//                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
//                transaction.replace(R.id.child_fragment_container, fragment).commit();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMeteor.removeCallbacks();
        mMeteor.addCallback(this);
        mMeteor.connect();


        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onConnect(boolean signedInAutomatically) {
        Log.d(TAG, "onConnect");
        mMeteor.subscribe(Constants.MongoCollection.NOTICE);
        mMeteor.subscribe(Constants.MongoCollection.STAFF);
        mMeteor.subscribe(Constants.MongoCollection.SIGN);

        NoticeManager.getInstance().init();
        mStaffs = new ArrayList<>();
        mSigns = new ArrayList<>();
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
//        Log.d(TAG, "onDataAdded:" + collectionName + " : " + newValuesJson);
        switch (collectionName) {
            case Constants.MongoCollection.NOTICE:
                try {
                    JSONObject obj = new JSONObject(newValuesJson);
                    String id = documentID;
                    String name = obj.getString("title");
                    String content = obj.getString("content");
                    String time = obj.getString("updateAt");
                    time = mUtils.getDateStr(time);

                    Log.d(TAG,"onDataAdded");
                    NoticeManager.getInstance().addNotice(new Notice(id, name, content, time));
                    mNoticeListFragment.updateList(NoticeManager.getInstance().getNotices());
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                break;
            case Constants.MongoCollection.STAFF:
                break;
            case Constants.MongoCollection.SIGN:
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
}
