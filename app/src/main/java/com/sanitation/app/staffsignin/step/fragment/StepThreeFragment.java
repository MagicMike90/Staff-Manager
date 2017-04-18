package com.sanitation.app.staffsignin.step.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.miguelbcr.ui.rx_paparazzo2.entities.FileData;
import com.miguelbcr.ui.rx_paparazzo2.entities.size.OriginalSize;
import com.miguelbcr.ui.rx_paparazzo2.entities.size.Size;
import com.sanitation.app.Constants;
import com.sanitation.app.camera.CameraData;
import com.sanitation.app.staffsignin.step.BaseFragment;
import com.sanitation.app.staffsignin.step.OnNavigationBarListener;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 4/17/17.
 */

public class StepThreeFragment extends BaseFragment implements Step , CameraData{
    private static final String TAG = "StepThreeFragment";
    private static final String DEPARTMENT = "department";

    private static final String LAYOUT_RESOURCE_ID_ARG_KEY = "messageResourceId";

    private String mSelectedDepartment;

    private OnNavigationBarListener onNavigationBarListener;

    private static final String STATE_FILES = "FILES";
    private ArrayList<FileData> fileDataList;
    private Size size;

    public static StepThreeFragment newInstance(@LayoutRes int layoutResId) {
        Bundle args = new Bundle();
        args.putInt(LAYOUT_RESOURCE_ID_ARG_KEY, layoutResId);
        StepThreeFragment fragment = new StepThreeFragment();
        fragment.setArguments(args);

        return fragment;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fileDataList = new ArrayList<>();
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_FILES)) {
                List files = (List) savedInstanceState.getSerializable(STATE_FILES);
                fileDataList.addAll(files);
            }
        }

        size = new OriginalSize();


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

    @Override
    public List<FileData> getFileDatas() {
        return null;
    }

    @Override
    public List<String> getFilePaths() {
        return null;
    }

    @Override
    public Size getSize() {
        return null;
    }
}
