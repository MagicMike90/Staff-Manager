package com.sanitation.app.staffmanagment.signstep.step.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.sanitation.app.Constants;
import com.sanitation.app.R;
import com.sanitation.app.staffmanagment.signstep.step.OnNavigationBarListener;
import com.sanitation.app.staffmanagment.signstep.step.StepInfoStorage;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

/**
 * Created by Michael on 4/17/17.
 */

public class StepOneFragment extends BaseFragment implements Step {
    private static final String TAG = "StepOneFragment";
    private static final String DEPARTMENT = "staff_department";

    private static final String LAYOUT_RESOURCE_ID_ARG_KEY = "messageResourceId";


    private String mSelectedDepartment;

    private OnNavigationBarListener onNavigationBarListener;

    public static StepOneFragment newInstance(@LayoutRes int layoutResId) {
        Bundle args = new Bundle();
        args.putInt(LAYOUT_RESOURCE_ID_ARG_KEY, layoutResId);
        StepOneFragment fragment = new StepOneFragment();
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

        MaterialSpinner department_spinner = (MaterialSpinner) view.findViewById(R.id.spinner_department);
        department_spinner.setItems(Constants.DEPARTMENT);
        department_spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                mSelectedDepartment = item;
                StepInfoStorage.getInstance().staff_department = item;
//                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });

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
            StepInfoStorage.getInstance().staff_department = mSelectedDepartment;
            onNavigationBarListener.onChangeEndButtonsEnabled(isAboveThreshold());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(DEPARTMENT, mSelectedDepartment);
        super.onSaveInstanceState(outState);
    }

}
