package com.sanitation.app.eventmanagement.event.upload;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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
import com.sanitation.app.eventmanagement.model.Event;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import com.zhihu.matisse.filter.Filter;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.riddhimanadib.formmaster.helper.FormBuildHelper;
import me.riddhimanadib.formmaster.model.FormElement;
import me.riddhimanadib.formmaster.model.FormHeader;
import me.riddhimanadib.formmaster.model.FormObject;

import static android.app.Activity.RESULT_OK;

/**
 * A placeholder fragment containing a simple view.
 */
public class UploadEventFragment extends Fragment {
    private final static String TAG = "UploadEventFragment";
    private RecyclerView mGridView;
    private RecyclerView mRecyclerView;
    private FormBuildHelper mFormBuilder;

    private EventUploadFragmentAdapter mAdapter;

    private List<Uri> mUris = new ArrayList<>();

    private static final int REQUEST_CODE_CHOOSE = 23;



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

        Button uploadImageBtn = (Button) view.findViewById(R.id.button_upload_image);
        uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
//                Matisse.from(getActivity())
//                        .choose(MimeType.allOf())
//                        .countable(true)
//                        .maxSelectable(8)
//                        .imageEngine(new PicassoEngine())
//                        .forResult(REQUEST_CODE_CHOOSE);

                Matisse.from(getActivity())
                        .choose(MimeType.of(MimeType.JPEG, MimeType.PNG, MimeType.GIF))
                        .countable(true)
                        .maxSelectable(9)
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .forResult(REQUEST_CODE_CHOOSE);
            }
        });

        setupForm();
        return view;
    }

    private void setupForm() {
        FormElement element11 = FormElement.createInstance().setType(FormElement.TYPE_EDITTEXT_TEXT_SINGLELINE).setTitle("事件描述");
        FormElement element12 = FormElement.createInstance().setType(FormElement.TYPE_EDITTEXT_TEXT_SINGLELINE).setTitle("事件类型");
        FormElement element13 = FormElement.createInstance().setType(FormElement.TYPE_EDITTEXT_TEXT_SINGLELINE).setTitle("上传人");
        ArrayList<String> departments = new ArrayList<>(Arrays.asList(Constants.DEPARTMENT));
        FormElement element14 = FormElement.createInstance().setType(FormElement.TYPE_SPINNER_DROPDOWN).setTitle("部门").setOptions(departments);
        FormElement element15 = FormElement.createInstance().setType(FormElement.TYPE_EDITTEXT_NUMBER).setTitle("扣除分数").setValue("0");


        List<FormObject> formItems = new ArrayList<>();
        formItems.add(element11);
        formItems.add(element12);
        formItems.add(element13);
        formItems.add(element14);
        formItems.add(element15);


        mFormBuilder.addFormElements(formItems);
        mFormBuilder.refreshView();
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
