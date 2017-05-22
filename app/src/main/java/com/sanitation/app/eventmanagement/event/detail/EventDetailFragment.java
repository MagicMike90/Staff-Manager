package com.sanitation.app.eventmanagement.event.detail;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sanitation.app.R;
import com.sanitation.app.factory.event.Event;
import com.sanitation.app.factory.event.EventManager;

/**
 * A placeholder fragment containing a simple view.
 */
public class EventDetailFragment extends Fragment {
    private static final String TAG = "SignDetailFragment";

    public static final String ARG_ID = "id";
    public static final String ARG_DESCRIPTION = "staff_name";
    public static final String ARG_TYPE = "type";
    public static final String ARG_STATUS = "status";
    public static final String ARG_UPLOAD_TIME = "upload_time";
    public static final String ARG_DURATION = "duration";


    private Event mEvents;
    public EventDetailFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Activity activity = this.getActivity();
//        if (getArguments().containsKey(ARG_DESCRIPTION)) {
//            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
//            if (appBarLayout != null) {
//                appBarLayout.setTitle(getArguments().getString(ARG_DESCRIPTION));
//            }
//        }

        if (getArguments().containsKey(ARG_ID)) {
            mEvents = EventManager.getInstance().getEvents(getArguments().getString(ARG_ID));
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
//        if (getArguments().containsKey(ARG_CONTENT)) {
//            TextView content = (TextView) rootView.findViewById(R.id.content);
//            content.setText(getArguments().getString(ARG_CONTENT));
//        }
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
}
