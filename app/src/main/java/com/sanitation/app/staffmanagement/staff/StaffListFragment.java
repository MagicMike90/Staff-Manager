package com.sanitation.app.staffmanagement.staff;

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
import com.sanitation.app.factory.staff.Staff;
import com.sanitation.app.factory.staff.StaffManager;
import com.sanitation.app.recyclerview.DividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorSingleton;
import im.delight.android.ddp.ResultListener;

public class StaffListFragment extends Fragment implements StaffFilterFragment.OnCloseListener {
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
        mViewAdapter = new StaffListFragmentAdapter(StaffManager.getInstance().getStaffs());
        mRecyclerView.setAdapter(mViewAdapter);
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

    public void updateList(List<Staff> staffs) {
        mViewAdapter.updateList(staffs);
    }
}
