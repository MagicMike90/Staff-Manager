package com.sanitation.app.StaffManagement.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.sanitation.app.Constants;
import com.sanitation.app.R;
import com.sanitation.app.Login.StaffInfo;
import com.sanitation.app.StaffManagement.fragments.StaffListFragment;
import com.sanitation.app.StaffManagement.models.Staff;
import com.sanitation.app.Utils.Utils;
import com.sanitation.app.Notice.activities.NoticeListActivity;

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

        mUtils = Utils.getInstance(this);
        mMeteor = MeteorSingleton.getInstance();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, NoticeListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_staff, menu);

        MenuItem searchViewItem = menu.findItem(R.id.action_search);
        final SearchView searchViewAndroidActionBar = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchViewAndroidActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchViewAndroidActionBar.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
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
        mMeteor.subscribe(Constants.MongoCollection.RELATED_STAFF, new Object[] {StaffInfo.getInstance().department});
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
