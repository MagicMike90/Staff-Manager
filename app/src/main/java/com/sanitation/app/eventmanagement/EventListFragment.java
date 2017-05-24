package com.sanitation.app.eventmanagement;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;
import com.sanitation.app.Constants;
import com.sanitation.app.R;
import com.sanitation.app.Utils;
import com.sanitation.app.eventmanagement.event.upload.UploadEventActivity;
import com.sanitation.app.factory.event.Event;
import com.sanitation.app.factory.event.EventManager;
import com.sanitation.app.factory.eventtype.EventType;
import com.sanitation.app.factory.eventtype.EventTypeManager;
import com.sanitation.app.recyclerview.DividerItemDecoration;
import com.sanitation.app.staffmanagement.sign.StaffSignInAndOutActivity;
import com.sanitation.app.staffmanagement.sign.step.StepInfoStorage;
import com.sanitation.app.widget.Fab;

import org.json.JSONException;
import org.json.JSONObject;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.MeteorSingleton;


public class EventListFragment extends Fragment implements MeteorCallback {
    private static final String TAG = "EventListFragment";


    private RecyclerView mRecyclerView;
    private EventListFragmentAdapter mViewAdapter;

    private SearchView searchView = null;
    private String mSearchbarHint;
    private SearchView.OnQueryTextListener queryTextListener;

    private Meteor mMeteor;
    private String mSubscribeId;

    private MaterialSheetFab materialSheetFab;
    private int statusBarColor;
    private OnClickListener mOnClickListener;

    private Context mContext;
    private Utils mUtils;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventListFragment() {
    }


    public static EventListFragment newInstance() {
        EventListFragment fragment = new EventListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMeteor = MeteorSingleton.getInstance();

        mOnClickListener = new OnClickListener();
        mContext = this.getContext();
        mUtils = Utils.getInstance(mContext);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);

        // Set the adapter

        Context context = view.getContext();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mViewAdapter = new EventListFragmentAdapter(EventManager.getInstance().getEvents());
        mRecyclerView.setAdapter(mViewAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));


        setupFab(view);

        return view;
    }

    private void setupFab(View view) {

        Fab fab = (Fab) view.findViewById(R.id.fab);
        View sheetView = view.findViewById(R.id.fab_sheet);
        View overlay = view.findViewById(R.id.overlay);
        int sheetColor = getResources().getColor(R.color.background_card);
        int fabColor = getResources().getColor(R.color.theme_accent);

        // Create material sheet FAB
        materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay, sheetColor, fabColor);

        // Set material sheet event listener
        materialSheetFab.setEventListener(new MaterialSheetFabEventListener() {
            @Override
            public void onShowSheet() {
                // Save current status bar color
                statusBarColor = getStatusBarColor();
                // Set darker status bar color to match the dim overlay
                setStatusBarColor(getResources().getColor(R.color.theme_primary_dark2));
            }

            @Override
            public void onHideSheet() {
                // Restore status bar color
                setStatusBarColor(statusBarColor);
            }
        });

        // Set material sheet item click listeners
        view.findViewById(R.id.upload_event).setOnClickListener(mOnClickListener);
    }

    private class OnClickListener implements View.OnClickListener {
        int type = R.string.title_activity_supervisor_check_in;

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.upload_event:
                    type = R.string.title_activity_supervisor_check_in;
                    StepInfoStorage.getInstance().staff_role = Constants.StaffRole.SUPERVISOR;
                    StepInfoStorage.getInstance().type = Constants.SignType.SIGN_IN;
                    break;
            }
            materialSheetFab.hideSheet();

            Intent intent = new Intent(EventListFragment.this.getContext(), UploadEventActivity.class);
            intent.putExtra(StaffSignInAndOutActivity.CHECK_IN_AN_OUT_TYPE, type);
            startActivity(intent);
        }
    }

    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setStatusBarColor(color);
        }
    }

    private int getStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getActivity().getWindow().getStatusBarColor();
        }
        return 0;
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
        Log.d(TAG, "onPause");
        mMeteor.removeCallback(this);
        mMeteor.disconnect();
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
        Log.d(TAG, "onConnect");
        EventManager.getInstance().init();
        mSubscribeId = mMeteor.subscribe(Constants.MongoCollection.EVENT);
        mMeteor.subscribe(Constants.MongoCollection.EVENT_TYPE);
    }

    @Override
    public void onDisconnect() {

    }

    @Override
    public void onException(Exception e) {

    }

    @Override
    public void onDataAdded(String collectionName, String documentID, String newValuesJson) {
        Log.d(TAG, "onDataAdded：" + collectionName);
        switch (collectionName) {
            case Constants.MongoCollection.EVENT:
                try {
                    JSONObject newVal = new JSONObject(newValuesJson);
                    String type = newVal.has("type") ? newVal.getString("type").toString() : "null";
                    String description = newVal.has("description") ? newVal.getString("description").toString() : "null";
                    String status = newVal.has("status") ? newVal.getString("status").toString() : "null";
                    String upload_time = newVal.has("upload_time") ? newVal.getString("upload_time") : "0";
                    String duration = newVal.has("duration") ? newVal.getString("duration") : "0";

                    upload_time = mUtils.getDateStr(upload_time);

                    EventManager.getInstance().addNotice(new Event(documentID, type, status, description, upload_time, duration));
                    mViewAdapter.updateList(EventManager.getInstance().getEvents());
                } catch (JSONException e) {
                    Log.d(TAG, Log.getStackTraceString(e));
                }
                break;
            case Constants.MongoCollection.EVENT_TYPE:
                Log.d(TAG, Constants.MongoCollection.EVENT_TYPE + "onDataAdded: " + newValuesJson);
                try {
                    JSONObject newVal = new JSONObject(newValuesJson);
                    String name = newVal.has("name") ? newVal.getString("name").toString() : "null";
                    String description = newVal.has("description") ? newVal.getString("description").toString() : "null";
                    String duration = newVal.has("duration") ? newVal.getString("duration") : "0";

                    EventTypeManager.getInstance().addEventType(new EventType(documentID, name, description, duration));
                } catch (JSONException e) {
                    Log.d(TAG, Log.getStackTraceString(e));
                }
                break;
        }
    }

    @Override
    public void onDataChanged(String collectionName, String documentID, String updatedValuesJson, String removedValuesJson) {

    }

    @Override
    public void onDataRemoved(String collectionName, String documentID) {

    }
}
