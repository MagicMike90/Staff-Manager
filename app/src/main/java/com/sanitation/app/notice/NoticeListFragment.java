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

import com.sanitation.app.Constants;
import com.sanitation.app.R;
import com.sanitation.app.Utils;
import com.sanitation.app.extras.recyclerview.DividerItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.MeteorSingleton;


public class NoticeListFragment extends Fragment implements MeteorCallback {
    private static final String TAG = "NoticeListFragment";


    private RecyclerView mRecyclerView;
    private NoticeListFragmentAdapter mViewAdapter;

    private Context mContext;
    private Utils mUtils;
    private Meteor mMeteor;

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
        mMeteor = MeteorSingleton.getInstance();
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
        mMeteor.removeCallback(this);
        mMeteor.disconnect();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onConnect(boolean signedInAutomatically) {
        Log.d(TAG, "onConnect");
        mMeteor.subscribe(Constants.MongoCollection.NOTICE);
        NoticeManager.getInstance().init();
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
        Log.d(TAG, "onDataAdded:" + collectionName + " : " + newValuesJson);

        if (collectionName.equals(Constants.MongoCollection.NOTICE)) {
            try {
                JSONObject obj = new JSONObject(newValuesJson);
                String id = documentID;
                String name = obj.getString("title");
                String content = obj.getString("content");
                String time = obj.getString("updateAt");
                time = mUtils.getDateStr(time);

                Log.d(TAG, name);

                NoticeManager.getInstance().addNotice(new Notice(id, name, content, time));
                this.updateList(NoticeManager.getInstance().getNotices());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDataChanged(String collectionName, String documentID, String updatedValuesJson, String removedValuesJson) {
        Log.d(TAG, collectionName);
        switch (collectionName) {
            case Constants.MongoCollection.NOTICE:
                break;
            case Constants.MongoCollection.STAFF:
                break;
            case Constants.MongoCollection.SIGN:
                break;
        }

    }

    @Override
    public void onDataRemoved(String collectionName, String documentID) {
        Log.d(TAG, "onDataRemoved");
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
