package com.sanitation.app.staffmanagement.notice;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sanitation.app.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class NoticeDetailFragment extends Fragment {
    private static final String TAG = "NoticeDetailFragment";

    public static final String ARG_ID = "id";
    public static final String ARG_TITLE = "description";
    public static final String ARG_CONTENT = "content";
    public static final String ARG_DATE = "date";

    public NoticeDetailFragment() {
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

        if (getArguments().containsKey(ARG_TITLE)) {
//            mNotice = NoticeManager.getInstance().getNotice(getArguments().getString(ARG_ID));
            String title = getArguments().getString(ARG_TITLE);
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(title);
            }
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notice_detail, container, false);
//        if (getArguments().containsKey(ARG_CONTENT)) {
//            TextView content = (TextView) rootView.findViewById(R.id.content);
//            content.setText(getArguments().getString(ARG_CONTENT));
//        }
        if (getArguments().containsKey(ARG_CONTENT)) {
            TextView content = (TextView) rootView.findViewById(R.id.content);
            content.setText(getArguments().getString(ARG_CONTENT));
        }
        return rootView;
    }
}
