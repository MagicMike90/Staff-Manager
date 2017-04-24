package com.sanitation.app.staffmanagment.staff;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.sanitation.app.R;
import com.sanitation.app.Utils;
import com.sanitation.app.recyclerview.DividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.MeteorSingleton;
import im.delight.android.ddp.ResultListener;
import im.delight.android.ddp.db.Collection;
import im.delight.android.ddp.db.Database;
import im.delight.android.ddp.db.Document;

public class StaffListFragment extends Fragment implements MeteorCallback, StaffFilterFragment.OnCloseListener {
    private static final String TAG = "StaffListFragment";
    private static final int DIALOG_REQ_CODE = 1;


    private RecyclerView mRecyclerView;
    private StaffListFragmentAdapter mViewAdapter;

    private Meteor mMeteor;
    private String mSubscribe;
    StaffFilterFragment mStaffFilterFragment;

    private String mFilterStaffName;
    private String mFilterDepartment;
    private String mFilterOnline;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StaffListFragment() {
    }


    public static StaffListFragment newInstance() {
        StaffListFragment fragment = new StaffListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mMeteor = MeteorSingleton.getInstance();
        mMeteor.addCallback(this);
        mMeteor.connect();

        mStaffFilterFragment = StaffFilterFragment.newInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staff_list, container, false);

        // Set the adapter
        Context context = view.getContext();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.staff_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(new StaffListFragmentAdapter(StaffManager.getInstance().getStaffs()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                mStaffFilterFragment.setTargetFragment(StaffListFragment.this, DIALOG_REQ_CODE);
                mStaffFilterFragment.show(getFragmentManager(), "StaffFilterFragment");
                return false;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        mMeteor.removeCallback(this);
        super.onPause();
        Log.d(TAG, "onPause");

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onConnect(boolean signedInAutomatically) {
        Log.d(TAG, "conConnect");
        mSubscribe = mMeteor.subscribe("staffs");
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
        Log.d(TAG, "onDataAdded collectionName: " + collectionName);

        Database database = mMeteor.getDatabase();
        int limit = 30;
        int offset = 0;


        Collection collection = database.getCollection(collectionName);
        Document[] documents = collection.find(limit, offset);
        if (documents.length != 0) {
            try {
                processData(documents);
            } catch (Exception e) {
                Log.d(TAG, Log.getStackTraceString(e));
            }
        }
    }

    @Override
    public void onDataChanged(String collectionName, String documentID, String updatedValuesJson, String removedValuesJson) {
        Log.d(TAG, "onDataChanged");
    }

    @Override
    public void onDataRemoved(String collectionName, String documentID) {

    }

    private void processData(Document[] documents) {
        StaffManager.getInstance().init();
        for (Document d : documents) {
            String name = d.getField("staff_name") != null ? d.getField("staff_name").toString() : "";
            String gender = d.getField("gender") != null ? d.getField("gender").toString() : "";
            String date = d.getField("join_work_date") != null ? d.getField("join_work_date").toString() : "0";


            Utils utils = Utils.getInstance(this.getContext());
            name = utils.getName(name);
            gender = utils.getGender(gender);
            date = utils.getDateStr(date);

            StaffManager.getInstance().addStaffs(new Staff(d.getId(), name, gender, date));
        }


        mViewAdapter = new StaffListFragmentAdapter(StaffManager.getInstance().getStaffs());
        mRecyclerView.setAdapter(mViewAdapter);
        mViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void OnCloseListener(JSONObject result) {

        try {
            mFilterStaffName = result.getString("filter_name");
            mFilterDepartment = result.getString("filter_department");
            mFilterOnline = result.getString("filter_online");

            mMeteor.call("staffs.find", new Object[]{mFilterStaffName, mFilterDepartment}, new ResultListener() {
                @Override
                public void onSuccess(String result) {
                    Log.d(TAG, "Call result: " + result);
                    try {
                        StaffManager.getInstance().init();
                        JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObj = jsonArray.getJSONObject(i);

                            String name = jsonObj.has("staff_name") ? jsonObj.getString("staff_name") : " ";
                            String gender = jsonObj.has("gender") ? jsonObj.getString("gender") : " ";
                            String date = jsonObj.has("join_work_date") ? jsonObj.getString("join_work_date") : "0";

                            Utils utils = Utils.getInstance(StaffListFragment.this.getContext());
                            name = utils.getName(name);
                            gender = utils.getGender(gender);
                            date = utils.getDateStr(date);

                            StaffManager.getInstance().addStaffs(new Staff(jsonObj.getString("_id"), name, gender, date));
                        }
                        mViewAdapter = new StaffListFragmentAdapter(StaffManager.getInstance().getStaffs());
                        mRecyclerView.setAdapter(mViewAdapter);
                        mViewAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(String error, String reason, String details) {
                    Log.d(TAG, "Error: " + error + " " + reason + " " + details);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, result.toString());
    }
}
