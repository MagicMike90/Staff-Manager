package com.sanitation.app.staffmanagement.staff;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sanitation.app.R;


public class StaffDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_STAFF_ID = "id";
    public static final String ARG_STAFF_NAME = "staff_name";
    public static final String ARG_STAFF_GENDER = "gender";
    public static final String ARG_STAFF_JOIN_WORK_DATE = "join_work_date";


    private Staff mStaffs;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StaffDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_STAFF_ID)) {
            mStaffs = StaffManager.getInstance().getStaff(getArguments().getString(ARG_STAFF_ID));
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(getArguments().getString(ARG_STAFF_ID));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.staff_detail, container, false);

        TextView staff_name =  (TextView) rootView.findViewById(R.id.staff_name);
        TextView gender =  (TextView) rootView.findViewById(R.id.gender);
        TextView join_work_date =  (TextView) rootView.findViewById(R.id.join_work_date);

        staff_name.setText(getArguments().getString(ARG_STAFF_NAME));
        gender.setText(getArguments().getString(ARG_STAFF_GENDER));
        join_work_date.setText(getArguments().getString(ARG_STAFF_JOIN_WORK_DATE));
        return rootView;
    }
}
