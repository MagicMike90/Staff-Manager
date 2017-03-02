package com.sanitation.app.notice;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sanitation.app.R;
import com.sanitation.app.recyclerview.DividerItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.MeteorSingleton;
import im.delight.android.ddp.db.Collection;
import im.delight.android.ddp.db.Database;
import im.delight.android.ddp.db.Document;
import im.delight.android.ddp.db.memory.InMemoryDatabase;


public class NoticeListFragment extends Fragment implements MeteorCallback {
    private static final String TAG = "NoticeListFragment";


    private RecyclerView mRecyclerView;
    private NoticeListFragmentAdapter mViewAdapter;

    private Meteor mMeteor;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NoticeListFragment() {
    }


    public static NoticeListFragment newInstance(int columnCount) {
        NoticeListFragment fragment = new NoticeListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!MeteorSingleton.hasInstance())
            MeteorSingleton.createInstance(this.getContext(), "ws://192.168.1.84:3000/websocket", new InMemoryDatabase());
        mMeteor = MeteorSingleton.getInstance();
        mMeteor.addCallback(this);
        mMeteor.connect();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notice_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            mRecyclerView.setAdapter(new NoticeListFragmentAdapter(NoticeManager.getInstance().getNotices()));
            mRecyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));
        }
        return view;
    }

    @Override
    public void onPause() {
        MeteorSingleton.getInstance().removeCallback(this);
        super.onDestroy();
        Log.d(TAG, "onDestroy");

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
        mMeteor.subscribe("notices");
    }

    @Override
    public void onDisconnect() {

    }

    @Override
    public void onException(Exception e) {

    }

    @Override
    public void onDataAdded(String collectionName, String documentID, String newValuesJson) {
        Log.d(TAG, "onConnect");

        try {
            Database database = mMeteor.getDatabase();
            Collection collection = database.getCollection("notices");

            int limit = 30;
            int offset = 0;
            Document[] documents = collection.find(limit, offset);
            for (Document d : documents) {
                String name = d.getField("title").toString();
                String content = d.getField("content").toString().replace("编辑时间","r");
                String time = d.getField("time").toString();

                NoticeManager.getInstance().addNotice(new Notice(name, content, time));
            }
        } catch (Exception e) {
            Log.d(TAG, Log.getStackTraceString(e));
        }
        mViewAdapter = new NoticeListFragmentAdapter(NoticeManager.getInstance().getNotices());
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
