package com.sanitation.app.staff;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.sanitation.app.Constants;
import com.sanitation.app.R;
import com.sanitation.app.Utils;
import com.sanitation.app.notice.Notice;
import com.sanitation.app.notice.NoticeActivity;
import com.sanitation.app.notice.NoticeManager;

import org.json.JSONException;
import org.json.JSONObject;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.MeteorSingleton;


public class StaffActivity extends AppCompatActivity implements MeteorCallback {
    private static final String TAG = "StaffActivity";
    private Utils mUtils;
    private Meteor mMeteor;

    private StaffListFragment mStaffListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mStaffListFragment = StaffListFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, mStaffListFragment).commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        mUtils = Utils.getInstance(this);
        mMeteor = MeteorSingleton.getInstance();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, NoticeActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        mMeteor.subscribe(Constants.MongoCollection.STAFF);
        StaffManager.getInstance().init();
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

        if (collectionName.equals(Constants.MongoCollection.STAFF)) {
            try {
                JSONObject staff = new JSONObject(newValuesJson);
                Log.d(TAG, staff.toString());
                String staff_id = staff.has("staff_id") ? staff.getString("staff_id") : " ";
                String name = staff.has("staff_name") ? staff.getString("staff_name") : " ";
                String gender = staff.has("gender") ? staff.getString("gender") : " ";
                String date = staff.has("join_work_date") ? staff.getString("join_work_date") : "0";

                name = mUtils.getName(name);
                gender = mUtils.getGender(gender);
                date = mUtils.getDateStr(date);

                StaffManager.getInstance().addStaffs(new Staff(documentID,staff_id, name, gender, date));
                mStaffListFragment.updateList(StaffManager.getInstance().getStaffs());
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
}
