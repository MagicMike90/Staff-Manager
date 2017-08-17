package com.sanitation.app.Event.eventmanagement.event.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.sanitation.app.R;

public class EventDetailActivity extends AppCompatActivity {
    private EventDetailFragment mEventDetailFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.event_toolbar);
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
            arguments.putString(EventDetailFragment.ARG_ID,
                    getIntent().getStringExtra(EventDetailFragment.ARG_ID));
//            arguments.putString(SignDetailFragment.ARG_DESCRIPTION,
//                    getIntent().getStringExtra(SignDetailFragment.ARG_DESCRIPTION));
//            arguments.putString(SignDetailFragment.ARG_CONTENT,
//                    getIntent().getStringExtra(SignDetailFragment.ARG_CONTENT));
//            arguments.putString(SignDetailFragment.ARG_DATE,
//                    getIntent().getStringExtra(SignDetailFragment.ARG_DATE));

            mEventDetailFragment = new EventDetailFragment();
            mEventDetailFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.notice_detail_container, mEventDetailFragment)
                    .commit();
        }
        Button finish_event =  (Button) findViewById(R.id.finish_event);
        finish_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                EventFinishFragment fragment = new EventFinishFragment();
//                Bundle arguments = new Bundle();
//                arguments.putString(EventDetailFragment.ARG_ID,
//                        getIntent().getStringExtra(EventDetailFragment.ARG_ID));
//                fragment.setArguments(arguments);
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.notice_detail_container, fragment)
//                        .commit();
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
        mEventDetailFragment.onActivityResult(requestCode, resultCode, data);
    }
}
