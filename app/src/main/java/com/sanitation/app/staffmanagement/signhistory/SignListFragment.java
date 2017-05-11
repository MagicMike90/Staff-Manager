package com.sanitation.app.staffmanagement.signhistory;

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
import com.sanitation.app.R;
import com.sanitation.app.factory.signhistory.SignHistory;
import com.sanitation.app.factory.signhistory.SignManager;
import com.sanitation.app.factory.staff.Staff;
import com.sanitation.app.recyclerview.DividerItemDecoration;
import com.sanitation.app.staffmanagement.sign.StaffSignInAndOutActivity;

import java.util.List;

import im.delight.android.ddp.MeteorSingleton;


public class SignListFragment extends Fragment{
    private static final String TAG = "SignListFragment";


    private RecyclerView mRecyclerView;
    private SignListFragmentAdapter mViewAdapter;

    private SearchView searchView = null;
    private String mSearchbarHint;
    private SearchView.OnQueryTextListener queryTextListener;


    private MaterialSheetFab materialSheetFab;
    private int statusBarColor;
    private OnClickListener mOnClickListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SignListFragment() {
    }


    public static SignListFragment newInstance() {
        SignListFragment fragment = new SignListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mOnClickListener = new OnClickListener();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_list, container, false);

        // Set the adapter

        Context context = view.getContext();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(new SignListFragmentAdapter(SignManager.getInstance().getSigns()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));


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


    private class OnClickListener implements View.OnClickListener {
        int type = R.string.title_activity_supervisor_check_in;

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fab_sheet_item_type_1:
                    type = R.string.title_activity_supervisor_check_in;
                    break;
                case R.id.fab_sheet_item_type_2:
                    type = R.string.title_activity_supervisor_check_out;
                    break;
                case R.id.fab_sheet_item_type_3:
                    type = R.string.title_activity_cleaner_check_in;
                    break;
                case R.id.fab_sheet_item_type_4:
                    type = R.string.title_activity_cleaner_check_out;
                    break;
            }
            materialSheetFab.hideSheet();

            Intent intent = new Intent(SignListFragment.this.getContext(), StaffSignInAndOutActivity.class);
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
        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause() {

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


    public void updateList(List<SignHistory> signs) {
        mViewAdapter.updateList(signs);
    }
}
