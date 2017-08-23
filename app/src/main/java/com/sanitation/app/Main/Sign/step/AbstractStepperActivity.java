/*
Copyright 2016 StepStone Services

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.sanitation.app.Main.Sign.step;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.sanitation.app.Login.StaffInfo;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.HashMap;
import java.util.Map;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorSingleton;
import im.delight.android.ddp.ResultListener;


public abstract class AbstractStepperActivity extends AppCompatActivity implements StepperLayout.StepperListener, OnNavigationBarListener {
    private static final String TAG = "AbstractStepperActivity";
    private static final String CURRENT_STEP_POSITION_KEY = "position";

    protected StepperLayout mStepperLayout;
    private Meteor mMeteor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        int startingStepPosition = savedInstanceState != null ? savedInstanceState.getInt(CURRENT_STEP_POSITION_KEY) : 0;

//        mStepperLayout = (StepperLayout) findViewById(R.id.stepperLayout);
        mStepperLayout.setAdapter(new SampleFragmentStepAdapter(getSupportFragmentManager(), this), startingStepPosition);
        mStepperLayout.setListener(this);

        mMeteor = MeteorSingleton.getInstance();
    }

    protected abstract int getLayoutResId();

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_STEP_POSITION_KEY, mStepperLayout.getCurrentStepPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        final int currentStepPosition = mStepperLayout.getCurrentStepPosition();
        if (currentStepPosition > 0) {
            mStepperLayout.onBackClicked();
        } else {
            finish();
        }
    }

    private void checkin() {
        try {
            Map<String, Object> user = new HashMap<String, Object>();
            user.put("id", mMeteor.getUserId());
            user.put("staff_name", StepInfoStorage.getInstance().staff_name);
            user.put("staff_id", StaffInfo.getInstance().staff_id);
            user.put("staff_role", StepInfoStorage.getInstance().staff_role);
            user.put("type", StepInfoStorage.getInstance().type);
            user.put("latitude", StepInfoStorage.getInstance().latitude);
            user.put("longitude", StepInfoStorage.getInstance().longitude);

            Object[] queryParams = {user};

            mMeteor.call("signInOut.mobile_insert", queryParams, new ResultListener() {
                @Override
                public void onSuccess(String result) {
                    Log.d(TAG, "Call result: " + result);
                }

                @Override
                public void onError(String error, String reason, String details) {
                    Log.d(TAG, "Error: " + error + " " + reason + " " + details);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCompleted(View completeButton) {
        checkin();
        Toast.makeText(this, "onCompleted!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onError(VerificationError verificationError) {
        Toast.makeText(this, "onError! -> " + verificationError.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStepSelected(int newStepPosition) {
        Toast.makeText(this, "onStepSelected! -> " + newStepPosition, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReturn() {
        finish();
    }

    @Override
    public void onChangeEndButtonsEnabled(boolean enabled) {
        mStepperLayout.setNextButtonVerificationFailed(!enabled);
        mStepperLayout.setCompleteButtonVerificationFailed(!enabled);
    }

}
