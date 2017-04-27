package com.sanitation.app.staffmanagement.sign.step.fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.sanitation.app.Constants;
import com.sanitation.app.R;
import com.sanitation.app.camera.PickerBuilder;
import com.sanitation.app.staffmanagement.sign.step.OnNavigationBarListener;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

/**
 * Created by Michael on 4/17/17.
 */

public class StepThreeFragment extends BaseFragment implements Step {
    private static final String TAG = "StepThreeFragment";
    private static final String DEPARTMENT = "department";

    private static final String LAYOUT_RESOURCE_ID_ARG_KEY = "messageResourceId";
    private String mSelectedDepartment;

    private OnNavigationBarListener onNavigationBarListener;


    private ImageView mImageView;
    private Button mCameraButton;
    private Button mPhotoButton;

    public static StepThreeFragment newInstance(@LayoutRes int layoutResId) {
        Bundle args = new Bundle();
        args.putInt(LAYOUT_RESOURCE_ID_ARG_KEY, layoutResId);
        StepThreeFragment fragment = new StepThreeFragment();
        fragment.setArguments(args);

        return fragment;
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(DEPARTMENT, mSelectedDepartment);
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

        mImageView = (ImageView) view.findViewById(R.id.iv_image);
        mCameraButton = (Button)view.findViewById(R.id.fab_camera_crop);
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PickerBuilder(getActivity(), PickerBuilder.SELECT_FROM_CAMERA)
                        .setOnImageReceivedListener(new PickerBuilder.onImageReceivedListener() {
                            @Override
                            public void onImageReceived(Uri imageUri) {
                                Toast.makeText(getActivity(),"Got image - " + imageUri,Toast.LENGTH_LONG).show();
                                mImageView.setImageURI(imageUri);
                            }
                        })
                        .setImageName("testImage")
                        .setImageFolderName("testFolder")
                        .withTimeStamp(false)
                        .setCropScreenColor(Color.BLACK)
                        .start();
            }
        });

        mPhotoButton = (Button)view.findViewById(R.id.fab_pickup_image);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PickerBuilder(getActivity(), PickerBuilder.SELECT_FROM_GALLERY)
                        .setOnImageReceivedListener(new PickerBuilder.onImageReceivedListener() {
                            @Override
                            public void onImageReceived(Uri imageUri) {
                                mImageView.setImageURI(imageUri);
                                Toast.makeText(getActivity(),"Got image - " + imageUri,Toast.LENGTH_LONG).show();
                            }
                        })
                        .setImageName("test")
                        .setImageFolderName("testFolder")
                        .setCropScreenColor(Color.CYAN)
                        .setOnPermissionRefusedListener(new PickerBuilder.onPermissionRefusedListener() {
                            @Override
                            public void onPermissionRefused() {

                            }
                        })
                        .start();
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
            onNavigationBarListener.onChangeEndButtonsEnabled(isAboveThreshold());
        }
    }

}
