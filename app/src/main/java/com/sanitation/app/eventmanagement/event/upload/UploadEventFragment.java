package com.sanitation.app.eventmanagement.event.upload;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sanitation.app.Constants;
import com.sanitation.app.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.riddhimanadib.formmaster.helper.FormBuildHelper;
import me.riddhimanadib.formmaster.model.FormElement;
import me.riddhimanadib.formmaster.model.FormHeader;
import me.riddhimanadib.formmaster.model.FormObject;

/**
 * A placeholder fragment containing a simple view.
 */
public class UploadEventFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private FormBuildHelper mFormBuilder;

    public UploadEventFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload_event, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mFormBuilder = new FormBuildHelper(getContext(), mRecyclerView);

        setupForm();
        return view;
    }
    private void setupForm() {

        FormHeader header1 = FormHeader.createInstance().setTitle("事件上传");
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
}
