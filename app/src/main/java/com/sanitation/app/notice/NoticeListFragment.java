package com.sanitation.app.notice;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.sanitation.app.MeteorDDP;
import com.sanitation.app.R;
import com.sanitation.app.recyclerview.DividerItemDecoration;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.db.Collection;
import im.delight.android.ddp.db.Database;
import im.delight.android.ddp.db.Document;


public class NoticeListFragment extends Fragment implements MeteorCallback {
    private static final String TAG = "NoticeListFragment";


    private RecyclerView mRecyclerView;
    private NoticeListFragmentAdapter mViewAdapter;

    private SearchView searchView = null;
    private String mSearchbarHint;
    private SearchView.OnQueryTextListener queryTextListener;

    private Meteor mMeteor;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NoticeListFragment() {
    }


    public static NoticeListFragment newInstance(String hint) {
        NoticeListFragment fragment = new NoticeListFragment();
        Bundle args = new Bundle();
        args.putString("hint",hint);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mMeteor = MeteorDDP.getInstance(this.getContext()).getConnection();
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
            mRecyclerView.setAdapter(new NoticeListFragmentAdapter(NoticeManager.getInstance().getNotice()));
            mRecyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));
        }
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            searchView.setQueryHint(mSearchbarHint);
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);

                    return true;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);

                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // Not implemented here
                return false;
            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
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
                String content = d.getField("content").toString().replace("编辑时间", "r");
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
