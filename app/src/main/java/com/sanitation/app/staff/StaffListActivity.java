package com.sanitation.app.staff;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.sanitation.app.R;

import com.sanitation.app.Utils;
import com.sanitation.app.recyclerview.DividerItemDecoration;
import com.sanitation.app.recyclerview.RecyclerClickListener;
import com.sanitation.app.recyclerview.RecyclerTouchListener;
import com.sanitation.app.staff.factory.Staff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.MeteorSingleton;
import im.delight.android.ddp.db.Collection;
import im.delight.android.ddp.db.Database;
import im.delight.android.ddp.db.Document;
import im.delight.android.ddp.db.memory.InMemoryDatabase;

/**
 * An activity representing a list of Staffs. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StaffDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class StaffListActivity extends AppCompatActivity implements MeteorCallback {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private static final String TAG = "StaffListActivity";
    private RecyclerView mRecyclerView;
    private StaffListRecyclerViewAdapter mViewAdapter;
    private boolean mTwoPane;
    private Meteor mMeteor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.staff_list);
        assert mRecyclerView != null;
//        setupRecyclerView((RecyclerView) mRecyclerView);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
//        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new RecyclerClickListener() {
//            @Override
//            public void onClick(View view, int position) {
////                Movie movie = movieList.get(position);
////                Toast.makeText(getApplicationContext(), movie.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//
//            }
//        }));


        if (findViewById(R.id.staff_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        if (!MeteorSingleton.hasInstance())
            MeteorSingleton.createInstance(this, "ws://192.168.1.84:3000/websocket", new InMemoryDatabase());
        mMeteor = MeteorSingleton.getInstance();
        mMeteor.addCallback(this);
        mMeteor.connect();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    private void setupRecyclerView() {
//        mViewAdapter = new StaffListRecyclerViewAdapter(this.STAFFS);
//        mRecyclerView.setAdapter(mViewAdapter);
//    }

    @Override
    public void onDestroy() {
        MeteorSingleton.getInstance().removeCallback(this);
        super.onDestroy();
        Log.d(TAG, "onDestroy");

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
        List<Staff> staffs = new ArrayList<Staff>();


        try {
            Database database = mMeteor.getDatabase();
            Collection collection = database.getCollection("staffs");

            int limit = 30;
            int offset = 0;
            Document[] documents = collection.find(limit, offset);
            for (Document d : documents) {
                String name = d.getField("staff_name").toString();
                String gender = d.getField("gender").toString();
                String date = d.getField("join_work_date") != null ? d.getField("join_work_date").toString() :"0";

                Utils utils = Utils.getInstance(this.getApplicationContext());
                name = utils.getName(name);
                gender =  utils.getGender(gender);
                date = utils.getDateStr(date);

                staffs.add(new Staff(d.getId(), name, gender, date));
            }
        } catch (Exception e) {
            Log.d(TAG, Log.getStackTraceString(e));
        }
        mViewAdapter = new StaffListRecyclerViewAdapter(staffs);
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

    public class StaffListRecyclerViewAdapter
            extends RecyclerView.Adapter<StaffListRecyclerViewAdapter.ViewHolder> {

        private final List<Staff> mValues;

        public StaffListRecyclerViewAdapter(List<Staff> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.staff_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mStaffNameView.setText(mValues.get(position).staff_name);
            holder.mGenderView.setText(mValues.get(position).gender);
            holder.mJoinWorkDateView.setText(mValues.get(position).join_work_date);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(StaffDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        StaffDetailFragment fragment = new StaffDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.staff_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, StaffDetailActivity.class);
                        intent.putExtra(StaffDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mStaffNameView;
            public final TextView mGenderView;
            public final TextView mJoinWorkDateView;
            public Staff mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mStaffNameView = (TextView) view.findViewById(R.id.staff_name);
                mGenderView = (TextView) view.findViewById(R.id.gender);
                mJoinWorkDateView = (TextView) view.findViewById(R.id.join_work_date);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mStaffNameView.getText() + "'";
            }
        }
    }
}
