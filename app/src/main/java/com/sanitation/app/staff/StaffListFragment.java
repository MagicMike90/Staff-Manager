package com.sanitation.app.staff;

import android.content.Context;
import android.net.Uri;
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

import com.sanitation.app.MeteorDDP;
import com.sanitation.app.R;
import com.sanitation.app.Utils;
import com.sanitation.app.recyclerview.DividerItemDecoration;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.db.Collection;
import im.delight.android.ddp.db.Database;
import im.delight.android.ddp.db.Document;

public class StaffListFragment extends Fragment implements MeteorCallback, StaffFilterFragment.OnCloseListener
{

    private static final String TAG = "StaffListFragment";


    private RecyclerView mRecyclerView;
    private StaffListFragmentAdapter mViewAdapter;

    private Meteor mMeteor;

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

        mMeteor =  MeteorDDP.getInstance(this.getContext()).getConnection();
        mMeteor.addCallback(this);
        mMeteor.connect();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stafflist_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            mRecyclerView.setAdapter(new StaffListFragmentAdapter(StaffManager.getInstance().getStaffs()));
            mRecyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));
        }
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
                StaffFilterFragment dialog = StaffFilterFragment.newInstance();
                dialog.show(getFragmentManager(), "StaffFilterFragment");
                return false;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        mMeteor.removeCallback(this);
        super.onDestroy();
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
        mMeteor.subscribe("staffs");
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
        Log.d(TAG, "onDataAdded");

        try {
            Database database = mMeteor.getDatabase();
            Collection collection = database.getCollection("staffs");
            StaffManager.getInstance().init();

            int limit = 30;
            int offset = 0;
            Document[] documents = collection.find(limit, offset);
            for (Document d : documents) {
                String name = d.getField("staff_name").toString();
                String gender = d.getField("gender").toString();
                String date = d.getField("join_work_date") != null ? d.getField("join_work_date").toString() : "0";

                Log.d(TAG, gender);

                Utils utils = Utils.getInstance(this.getContext());
                name = utils.getName(name);
                gender = utils.getGender(gender);
                date = utils.getDateStr(date);

                StaffManager.getInstance().addStaffs(new Staff(d.getId(), name, gender, date));
            }
        } catch (Exception e) {
            Log.d(TAG, Log.getStackTraceString(e));
        }
        mViewAdapter = new StaffListFragmentAdapter( StaffManager.getInstance().getStaffs());
        mRecyclerView.setAdapter(mViewAdapter);
        mViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDataChanged(String collectionName, String documentID, String updatedValuesJson, String removedValuesJson) {
        Log.d(TAG, "onDataChanged");
    }

    @Override
    public void onDataRemoved(String collectionName, String documentID) {

    }

    @Override
    public void OnCloseListener(Uri uri) {

    }
}
