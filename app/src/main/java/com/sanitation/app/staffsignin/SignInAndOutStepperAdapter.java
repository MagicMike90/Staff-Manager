package com.sanitation.app.staffsignin;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

/**
 * Created by Michael on 4/16/17.
 */

public class SignInAndOutStepperAdapter extends AbstractFragmentStepAdapter {
    private static String CURRENT_STEP_POSITION_KEY = "0";

    public SignInAndOutStepperAdapter(FragmentManager fm, Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(int position) {
        final SignInAndOutStepFragment step = new SignInAndOutStepFragment();
        Bundle b = new Bundle();
        b.putInt(CURRENT_STEP_POSITION_KEY, position);
        step.setArguments(b);
        return step;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        return new StepViewModel.Builder(context)
                .setTitle("can be a CharSequence instead") //can be a CharSequence instead
                .create();
    }
}
