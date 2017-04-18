package com.sanitation.app.staffsignin.step.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.sanitation.app.Constants;
import com.sanitation.app.R;
import com.sanitation.app.staffsignin.step.BaseFragment;
import com.sanitation.app.staffsignin.step.OnNavigationBarListener;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

/**
 * Created by Michael on 4/17/17.
 */

public class StepTwoFragment extends BaseFragment implements Step {
    private static final String TAG = "StepTwoFragment";
    private static final String DEPARTMENT = "department";

    private static final String LAYOUT_RESOURCE_ID_ARG_KEY = "messageResourceId";

    private String mSelectedDepartment;

    private OnNavigationBarListener onNavigationBarListener;

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

        mSelectedDepartment = Constants.DEPARTMENT[0];
        if (savedInstanceState != null) {
            mSelectedDepartment = savedInstanceState.getString(DEPARTMENT);
        }

        updateNavigationBar();

    }

    @Override
    protected int getLayoutResId() {
        return getArguments().getInt(LAYOUT_RESOURCE_ID_ARG_KEY);
    }

    @Override
    public VerificationError verifyStep() {
        return isAboveThreshold() ? null : new VerificationError("It is empty!");
    }

    private boolean isAboveThreshold() {
        return mSelectedDepartment != "";
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
        outState.putString(DEPARTMENT, mSelectedDepartment);
        super.onSaveInstanceState(outState);
    }
}
