package com.sanitation.app.staffmanagement.notice;

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
import com.sanitation.app.staffmanagement.sign.StaffSignInAndOutActivity;
import com.sanitation.app.staffmanagement.sign.step.StepInfoStorage;
import com.sanitation.app.widget.Fab;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.MeteorSingleton;
import im.delight.android.ddp.db.Collection;
import im.delight.android.ddp.db.Database;
import im.delight.android.ddp.db.Document;


public class NoticeListFragment extends Fragment {
    private static final String TAG = "NoticeListFragment";


    private RecyclerView mRecyclerView;
    private NoticeListFragmentAdapter mViewAdapter;

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

        mMeteor = MeteorSingleton.getInstance();
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
        mViewAdapter = new NoticeListFragmentAdapter(NoticeManager.getInstance().getNotice());
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
                    StepInfoStorage.getInstance().type = Constants.SignType.SIGN_IN;
                    break;
                case R.id.fab_sheet_item_type_2:
                    type = R.string.title_activity_supervisor_check_out;
                    StepInfoStorage.getInstance().type = Constants.SignType.SIGN_OUT;
                    StepInfoStorage.getInstance().staff_role = Constants.StaffRole.SUPERVISOR;
                    break;
                case R.id.fab_sheet_item_type_3:
                    type = R.string.title_activity_cleaner_check_in;
                    StepInfoStorage.getInstance().type = Constants.SignType.SIGN_IN;
                    StepInfoStorage.getInstance().staff_role = Constants.StaffRole.CLEANER;

                    break;
                case R.id.fab_sheet_item_type_4:
                    type = R.string.title_activity_cleaner_check_out;
                    StepInfoStorage.getInstance().type = Constants.SignType.SIGN_OUT;
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

    public void updateList(List<Notice> notices) {
        mViewAdapter.updateList(notices);
    }
}
