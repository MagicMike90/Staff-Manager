package com.sanitation.app.eventmanagement;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.sanitation.app.eventmanagement.model.Event;
import com.sanitation.app.eventmanagement.model.EventManager;
import com.sanitation.app.recyclerview.DividerItemDecoration;
import com.sanitation.app.staffmanagement.sign.StaffSignInAndOutActivity;
import com.sanitation.app.staffmanagement.sign.step.StepInfoStorage;
import com.sanitation.app.widget.Fab;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.MeteorSingleton;
import im.delight.android.ddp.db.Collection;
import im.delight.android.ddp.db.Database;
import im.delight.android.ddp.db.Document;


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
//        setHasOptionsMenu(true);
//        mMeteor = MeteorDDP.getInstance(this.getContext()).getConnection();

        mMeteor = MeteorSingleton.getInstance();
//        mMeteor = new Meteor(getContext(), Constants.METEOR_SERVER_SOCKET,new InMemoryDatabase());

        mOnClickListener = new OnClickListener();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);

        // Set the adapter

        Context context = view.getContext();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(new EventListFragmentAdapter(EventManager.getInstance().getEvents()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        setupFab(view);

        return view;
    }
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        MenuItem searchItem = menu.findItem(R.id.action_search);
//        searchItem.setVisible(true);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_search:
//                SignFilterDialogFragment dialog = SignFilterDialogFragment.newInstance();
//                dialog.show(getFragmentManager(), "dialog");
//
//                return false;
//            default:
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

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
        mMeteor.addCallback(this);
        mMeteor.connect();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        mMeteor.removeCallback(this);
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
        Log.d(TAG, "onConnect");

        mSubscribeId = mMeteor.subscribe("events");
    }

    @Override
    public void onDisconnect() {

    }

    @Override
    public void onException(Exception e) {

    }

    @Override
    public void onDataAdded(String collectionName, String documentID, String newValuesJson) {
        Log.d(TAG, "onDataAdded");

        try {
            Database database = mMeteor.getDatabase();
            Collection collection = database.getCollection("events");
            EventManager.getInstance().init();

            Document[] documents = collection.find();
            for (Document d : documents) {
                Log.d(TAG,d.toString());
                String description = d.getField("description").toString();
                String status = d.getField("status").toString();
                String upload_time = d.getField("upload_time").toString();

                Utils utils = Utils.getInstance(this.getContext());

                upload_time = utils.getDateStr(upload_time);

                EventManager.getInstance().addNotice(new Event(d.getId(), description,description, upload_time,status));
            }
        } catch (Exception e) {
            Log.d(TAG, Log.getStackTraceString(e));
        }
        mViewAdapter = new EventListFragmentAdapter(EventManager.getInstance().getEvents());
        mRecyclerView.setAdapter(mViewAdapter);
        mViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDataChanged(String collectionName, String documentID, String updatedValuesJson, String removedValuesJson) {

    }

    @Override
    public void onDataRemoved(String collectionName, String documentID) {

    }
}
