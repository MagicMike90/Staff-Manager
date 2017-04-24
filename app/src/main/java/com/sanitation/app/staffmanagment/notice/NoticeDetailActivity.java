package com.sanitation.app.staffmanagment.notice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.sanitation.app.R;

public class NoticeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.notice_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(NoticeDetailFragment.ARG_ID,
                    getIntent().getStringExtra(NoticeDetailFragment.ARG_ID));
//            arguments.putString(NoticeDetailFragment.ARG_TITLE,
//                    getIntent().getStringExtra(NoticeDetailFragment.ARG_TITLE));
//            arguments.putString(NoticeDetailFragment.ARG_CONTENT,
//                    getIntent().getStringExtra(NoticeDetailFragment.ARG_CONTENT));
//            arguments.putString(NoticeDetailFragment.ARG_DATE,
//                    getIntent().getStringExtra(NoticeDetailFragment.ARG_DATE));

            NoticeDetailFragment fragment = new NoticeDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.notice_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, NoticeDetailActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
