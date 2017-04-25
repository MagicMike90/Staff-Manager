package com.sanitation.app.staffmanagment.notice;

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
import com.sanitation.app.recyclerview.DividerItemDecoration;
import com.sanitation.app.staffmanagment.sign.StaffSignInAndOutActivity;
import com.sanitation.app.staffmanagment.sign.step.StepInfoStorage;
import com.sanitation.app.widget.Fab;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.MeteorSingleton;
import im.delight.android.ddp.db.Collection;
import im.delight.android.ddp.db.Database;
import im.delight.android.ddp.db.Document;



public class NoticeListFragment extends Fragment implements MeteorCallback {
    private static final String TAG = "SignListFragment";


    private RecyclerView mRecyclerView;
    private NoticeListFragmentAdapter mViewAdapter;

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
    public NoticeListFragment() {
    }


    public static NoticeListFragment newInstance() {
        NoticeListFragment fragment = new NoticeListFragment();
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
        mMeteor.addCallback(this);

        mMeteor.connect();
        mOnClickListener = new OnClickListener();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notice_list, container, false);

        // Set the adapter

        Context context = view.getContext();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(new NoticeListFragmentAdapter(NoticeManager.getInstance().getNotice()));
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
        view.findViewById(R.id.fab_sheet_item_type_1).setOnClickListener(mOnClickListener);
        view.findViewById(R.id.fab_sheet_item_type_2).setOnClickListener(mOnClickListener);
        view.findViewById(R.id.fab_sheet_item_type_3).setOnClickListener(mOnClickListener);
        view.findViewById(R.id.fab_sheet_item_type_4).setOnClickListener(mOnClickListener);
    }

    private class OnClickListener implements View.OnClickListener {
        int type = R.string.title_activity_supervisor_check_in;
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fab_sheet_item_type_1:
                    type = R.string.title_activity_supervisor_check_in;
                    StepInfoStorage.getInstance().staff_role = Constants.StaffRole.SUPERVISOR;
                    break;
                case R.id.fab_sheet_item_type_2:
                    type = R.string.title_activity_supervisor_check_out;
                    StepInfoStorage.getInstance().staff_role = Constants.StaffRole.SUPERVISOR;
                    break;
                case R.id.fab_sheet_item_type_3:
                    type = R.string.title_activity_cleaner_check_in;
                    StepInfoStorage.getInstance().staff_role = Constants.StaffRole.CLEANER;
                    break;
                case R.id.fab_sheet_item_type_4:
                    type = R.string.title_activity_cleaner_check_out;
                    StepInfoStorage.getInstance().staff_role = Constants.StaffRole.CLEANER;
                    break;
            }
            materialSheetFab.hideSheet();

            Intent intent = new Intent(NoticeListFragment.this.getContext(), StaffSignInAndOutActivity.class);
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
        Log.d(TAG, "onConnect");

        mSubscribeId = mMeteor.subscribe("notices");
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
            Collection collection = database.getCollection("notices");
            NoticeManager.getInstance().init();

            int limit = 30;
            int offset = 0;
            Document[] documents = collection.find(limit, offset);
            for (Document d : documents) {
                String name = d.getField("title").toString();
                String content = d.getField("content").toString().replace("编辑时间", "");
                String time = d.getField("time").toString();

                NoticeManager.getInstance().addNotice(new Notice(d.getId(), name, content, time));
            }
        } catch (Exception e) {
            Log.d(TAG, Log.getStackTraceString(e));
        }
        mViewAdapter = new NoticeListFragmentAdapter(NoticeManager.getInstance().getNotice());
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
