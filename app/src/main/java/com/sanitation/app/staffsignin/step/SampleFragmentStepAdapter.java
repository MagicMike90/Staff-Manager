package com.sanitation.app.staffsignin.step;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.sanitation.app.R;
import com.sanitation.app.staffsignin.step.fragment.StepOneFragment;
import com.sanitation.app.staffsignin.step.fragment.StepThreeFragment;
import com.sanitation.app.staffsignin.step.fragment.StepTwoFragment;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

public class SampleFragmentStepAdapter extends AbstractFragmentStepAdapter {

    public SampleFragmentStepAdapter(FragmentManager fm, Context context) {
        super(fm, context);
    }


    @Override
    public StepViewModel getViewModel(int position) {
        int title = R.string.stepper_tab_one;

        switch (position) {
            case 0:
                title = R.string.stepper_tab_one;
                break;
            case 1:
                title = R.string.stepper_tab_two;
                break;
            case 2:
                title = R.string.stepper_tab_three;
                break;
            default:
        }
        StepViewModel stepView  =  new StepViewModel.Builder(context)
                .setTitle(title)
                .create();

        return stepView;
    }

    @Override
    public Step createStep(int position) {
        switch (position) {
            case 0:
                return StepOneFragment.newInstance(R.layout.stepper_fragment_step_one);
            case 1:
                return StepTwoFragment.newInstance(R.layout.stepper_fragment_step_two);
            case 2:
                return StepThreeFragment.newInstance(R.layout.stepper_fragment_step_three);
            default:
                throw new IllegalArgumentException("Unsupported position: " + position);
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}