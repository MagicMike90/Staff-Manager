package com.sanitation.app.staff.staffmanagement.sign.step.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.sanitation.app.R;
import com.sanitation.app.staff.staffmanagement.sign.step.OnNavigationBarListener;
import com.sanitation.app.staff.staffmanagement.sign.step.StepInfoStorage;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

/**
 * Created by Michael on 4/17/17.
 */

public class StepTwoFragment extends BaseFragment implements Step {
    private static final String TAG = "StepTwoFragment";
    private static final String STAFF_NAME = "staff_name";

    private static final String LAYOUT_RESOURCE_ID_ARG_KEY = "messageResourceId";

//    private String mSelectedDepartment;

    private OnNavigationBarListener onNavigationBarListener;

    private EditText mNameView;
    private String mStaffName;

    public static StepTwoFragment newInstance(@LayoutRes int layoutResId) {
        Bundle args = new Bundle();
        args.putInt(LAYOUT_RESOURCE_ID_ARG_KEY, layoutResId);
        StepTwoFragment fragment = new StepTwoFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNavigationBarListener) {
            onNavigationBarListener = (OnNavigationBarListener) context;
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        mSelectedDepartment = Constants.DEPARTMENT[0];
        mNameView = (EditText) view.findViewById(R.id.staff_name);
        mStaffName = mNameView.getText() != null ? mNameView.getText().toString() : "";

        if (savedInstanceState != null) {
            mStaffName = savedInstanceState.getString(STAFF_NAME);

        }

        updateNavigationBar();

    }

    @Override
    protected int getLayoutResId() {
        return getArguments().getInt(LAYOUT_RESOURCE_ID_ARG_KEY);
    }

    @Override
    public VerificationError verifyStep() {
        Log.d(TAG, String.valueOf(isAboveThreshold()));

        return isAboveThreshold() ? null : new VerificationError("姓名不能为空!");
    }

    private boolean isAboveThreshold() {
        mStaffName = mNameView.getText().toString();
        StepInfoStorage.getInstance().staff_name = mStaffName;
        Log.d(TAG,mStaffName);
        return !mStaffName.isEmpty();
    }

    @Override
    public void onSelected() {
        updateNavigationBar();
    }

    @Override
    public void onError(VerificationError error) {
//        button.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.shake_error));
    }

    private void updateNavigationBar() {
        if (onNavigationBarListener != null) {
            onNavigationBarListener.onChangeEndButtonsEnabled(isAboveThreshold());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(STAFF_NAME, mStaffName);
        super.onSaveInstanceState(outState);
    }
}
