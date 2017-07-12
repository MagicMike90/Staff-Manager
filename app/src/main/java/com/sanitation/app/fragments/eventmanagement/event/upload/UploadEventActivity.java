package com.sanitation.app.fragments.eventmanagement.event.upload;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.sanitation.app.R;
import com.sanitation.app.fragments.eventmanagement.event.detail.EventDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class UploadEventActivity extends AppCompatActivity {
    private final static String TAG = "UploadEventActivity";

    private static final int REQUEST_CODE_CHOOSE = 23;
    UploadEventFragment mUploadEventFragment;

    private List<Uri> mUris = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_event);

        setupToolBar();

        mUploadEventFragment = new UploadEventFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mUploadEventFragment)
                .commit();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, EventDetailActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        mUploadEventFragment.onActivityResult(requestCode, resultCode, data);

//        super.onActivityResult(requestCode, resultCode, data);
//        Log.d(TAG, "onActivityResult");
//        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
//            mUris = Matisse.obtainResult(data);
//            Log.d(TAG, "mSelected: " + mUris);
//        }
    }
}
