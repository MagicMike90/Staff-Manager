package com.sanitation.app.Event.eventmanagement.event.detail;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.sanitation.app.R;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.ArrayList;
import java.util.List;


public class EventFinishActivity extends AppCompatActivity {
    private static final String TAG = "EventFinishActivity";
    private RecyclerView mGridView;
    private EventFinishGridAdapter mAdapter;

    private List<Uri> mUris = new ArrayList<>();
    private static final int REQUEST_CODE_CHOOSE = 23;
    private static int MAX_SELECTED_IMAGE_SIZE = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_finish);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        mGridView = (RecyclerView) findViewById(R.id.gridView);
        int numberOfColumns = 4;
        mGridView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        mGridView.setHasFixedSize(true);
        mAdapter = new EventFinishGridAdapter(this, mUris);
        mGridView.setAdapter(mAdapter);

        Button uploadImageBtn = (Button) findViewById(R.id.button_camera);
        uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Matisse.from(EventFinishActivity.this)
                        .choose(MimeType.of(MimeType.JPEG, MimeType.PNG, MimeType.GIF))
                        .countable(true)
                        .maxSelectable(MAX_SELECTED_IMAGE_SIZE - mUris.size())
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .forResult(REQUEST_CODE_CHOOSE);
            }
        });

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mUris = Matisse.obtainResult(data);
            Log.d(TAG, "mSelected: " + mUris);
            mAdapter.setData(mUris);
        }
        getSupportActionBar().show();
    }
}
