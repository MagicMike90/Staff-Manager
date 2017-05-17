package com.sanitation.app.eventmanagement.event.upload;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sanitation.app.Constants;
import com.sanitation.app.R;
import com.sanitation.app.StaffInfo;
import com.sanitation.app.factory.eventtype.EventType;
import com.sanitation.app.factory.eventtype.EventTypeManager;
import com.sanitation.app.staffmanagement.sign.step.StepInfoStorage;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.MeteorSingleton;
import im.delight.android.ddp.ResultListener;
import me.riddhimanadib.formmaster.helper.FormBuildHelper;
import me.riddhimanadib.formmaster.model.FormElement;
import me.riddhimanadib.formmaster.model.FormObject;

import static android.app.Activity.RESULT_OK;

/**
 * A placeholder fragment containing a simple view.
 */
public class UploadEventFragment extends Fragment{
    private static final String TAG = "UploadEventFragment";
    private RecyclerView mGridView;
    private RecyclerView mRecyclerView;
    private FormBuildHelper mFormBuilder;
    private List<FormObject> formItems = new ArrayList<>();

    private EventUploadFragmentAdapter mAdapter;

    private List<Uri> mUris = new ArrayList<>();
    private static final int REQUEST_CODE_CHOOSE = 23;
    private static int MAX_SELECTED_IMAGE_SIZE = 8;


    private  Meteor mMeteor;
    public UploadEventFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload_event, container, false);

        mGridView = (RecyclerView) view.findViewById(R.id.gridView);
        int numberOfColumns = 4;
        mGridView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
        mGridView.setHasFixedSize(true);
        mAdapter = new EventUploadFragmentAdapter(getContext(), mUris);
        mGridView.setAdapter(mAdapter);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mFormBuilder = new FormBuildHelper(getContext(), mRecyclerView);

        final Button uploadImageBtn = (Button) view.findViewById(R.id.button_upload_image);
        uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

                Matisse.from(getActivity())
                        .choose(MimeType.of(MimeType.JPEG, MimeType.PNG, MimeType.GIF))
                        .countable(true)
                        .maxSelectable(MAX_SELECTED_IMAGE_SIZE - mUris.size())
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .forResult(REQUEST_CODE_CHOOSE);
            }
        });

        Button confirmBtn = (Button) view.findViewById(R.id.button_confirm);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadEvent();
            }
        });

        mMeteor = MeteorSingleton.getInstance();

        setupForm();
        return view;
    }

    private void uploadEvent() {
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

    private void setupForm() {
        FormElement element11 = FormElement.createInstance().setType(FormElement.TYPE_EDITTEXT_TEXT_SINGLELINE).setTitle("事件描述");
        FormElement element12 = FormElement.createInstance().setType(FormElement.TYPE_SPINNER_DROPDOWN).setTitle("事件类型").setOptions(EventTypeManager.getInstance().getEventTypeNames());
        FormElement element13 = FormElement.createInstance().setType(FormElement.TYPE_EDITTEXT_TEXT_SINGLELINE).setTitle("上传人");
        ArrayList<String> departments = new ArrayList<>(Arrays.asList(Constants.DEPARTMENT));
        FormElement element14 = FormElement.createInstance().setType(FormElement.TYPE_SPINNER_DROPDOWN).setTitle("部门").setOptions(departments);
        FormElement element15 = FormElement.createInstance().setType(FormElement.TYPE_EDITTEXT_NUMBER).setTitle("扣除分数").setValue("0");

        formItems.add(element11);
        formItems.add(element12);
        formItems.add(element13);
        formItems.add(element14);
        formItems.add(element15);

        mFormBuilder.addFormElements(formItems);
        mFormBuilder.refreshView();
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mUris = Matisse.obtainResult(data);
            Log.d(TAG, "mSelected: " + mUris);
            mAdapter.setData(mUris);
        }
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

}
