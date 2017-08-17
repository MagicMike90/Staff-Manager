package com.sanitation.app.Notice.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sanitation.app.Notice.models.Notice;
import com.sanitation.app.Notice.adapters.NoticeListFragmentAdapter;
import com.sanitation.app.Notice.models.NoticeManager;
import com.sanitation.app.R;
import com.sanitation.app.Utils.Utils;
import com.sanitation.app.CustomComponent.recyclerview.DividerItemDecoration;

import java.util.List;


public class NoticeListFragment extends Fragment {
    private static final String TAG = "NoticeListFragment";


    private RecyclerView mRecyclerView;
    private NoticeListFragmentAdapter mViewAdapter;

    private Context mContext;
    private Utils mUtils;

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

        mContext = this.getContext();
        mUtils = Utils.getInstance(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notice_list, container, false);

        // Set the adapter
        Context context = view.getContext();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mViewAdapter = new NoticeListFragmentAdapter(NoticeManager.getInstance().getNotices());
        mRecyclerView.setAdapter(mViewAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));

        return view;
    }

    public void updateList(List<Notice> notices) {
        Log.d(TAG, "updateList: " + notices.toString());
        mViewAdapter.updateList(notices);
    }
}
