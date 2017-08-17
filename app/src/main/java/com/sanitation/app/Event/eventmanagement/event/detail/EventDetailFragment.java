package com.sanitation.app.Event.eventmanagement.event.detail;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sanitation.app.R;
import com.sanitation.app.factory.event.Event;
import com.sanitation.app.factory.event.EventManager;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A placeholder fragment containing a simple view.
 */
public class EventDetailFragment extends Fragment {
    private static final String TAG = "EventDetailFragment";

    private RecyclerView mGridView;

    public static final String ARG_ID = "id";
    public static final String ARG_DESCRIPTION = "staff_name";
    public static final String ARG_TYPE = "type";
    public static final String ARG_STATUS = "status";
    public static final String ARG_UPLOAD_TIME = "upload_time";
    public static final String ARG_DURATION = "duration";

    private EventFinishGridAdapter mAdapter;
    private List<Uri> mUris = new ArrayList<>();
    private static final int REQUEST_CODE_CHOOSE = 23;
    private static int MAX_SELECTED_IMAGE_SIZE = 8;

    private Event mEvents;
    public EventDetailFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Activity activity = this.getActivity();

        if (getArguments().containsKey(ARG_ID)) {
            Log.d(TAG,getArguments().toString());
            mEvents = EventManager.getInstance().getEvent(getArguments().getString(ARG_ID));
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mEvents.description);
            }
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_detail, container, false);
        mGridView = (RecyclerView) rootView.findViewById(R.id.gridView);
        int numberOfColumns = 4;
        mGridView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
        mGridView.setHasFixedSize(true);
        mAdapter = new EventFinishGridAdapter(getContext(), mUris);
        mGridView.setAdapter(mAdapter);


        final Button uploadImageBtn = (Button) rootView.findViewById(R.id.button_upload_image);
        uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

                Matisse.from(getActivity())
                        .choose(MimeType.of(MimeType.JPEG, MimeType.PNG, MimeType.GIF))
                        .countable(true)
                        .maxSelectable(MAX_SELECTED_IMAGE_SIZE - mUris.size())
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .forResult(REQUEST_CODE_CHOOSE);
            }
        });

        if (getArguments().containsKey(ARG_ID)) {
            TextView title = (TextView) rootView.findViewById(R.id.description);
            title.setText(mEvents.description);


            TextView status = (TextView) rootView.findViewById(R.id.status);
            status.setText(mEvents.status);

            TextView date = (TextView) rootView.findViewById(R.id.upload_time);
            date.setText(mEvents.upload_time);

            TextView content = (TextView) rootView.findViewById(R.id.duration);
            content.setText(mEvents.duration);

        }
        return rootView;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mUris = Matisse.obtainResult(data);
            Log.d(TAG, "mSelected: " + mUris);
            mAdapter.setData(mUris);
        }
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }
}
