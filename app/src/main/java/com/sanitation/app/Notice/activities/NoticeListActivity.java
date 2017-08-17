package com.sanitation.app.Notice.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.sanitation.app.Notice.models.NoticeManager;
import com.sanitation.app.Notice.fragments.NoticeListFragment;
import com.sanitation.app.Notice.models.Notice;
import com.sanitation.app.R;
import com.sanitation.app.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorSingleton;
import im.delight.android.ddp.ResultListener;

public class NoticeListActivity extends AppCompatActivity {
    private static final String TAG = "NoticeListActivity";

    private NoticeListFragment mFragment;
    private Meteor mMeteor;
    private Utils mUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mFragment = NoticeListFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, mFragment).commit();

        mMeteor = MeteorSingleton.getInstance();
        mUtils = Utils.getInstance(this);
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
    protected void onResume() {
        super.onResume();
        mMeteor.call("notices.getAll", new ResultListener() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonarray = new JSONArray(result);
                    for (int i = 0; i < jsonarray.length(); i++){
                        JSONObject obj = jsonarray.getJSONObject(i);
                        String id = obj.getString("_id");
                        String name = obj.getString("title");
                        String content = obj.getString("content");
                        String time = obj.getString("updateAt");
                        time = mUtils.getDateStr(time);

                        NoticeManager.getInstance().addNotice(new Notice(id, name, content, time));
                        mFragment.updateList(NoticeManager.getInstance().getNotices());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error, String reason, String details) {
                Log.d(TAG, "Error: " + error + " " + reason + " " + details);
            }
        });
    }
}
