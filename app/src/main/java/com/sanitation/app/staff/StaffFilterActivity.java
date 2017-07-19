package com.sanitation.app.staff;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.sanitation.app.Constants;
import com.sanitation.app.MainFragment;
import com.sanitation.app.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.riddhimanadib.formmaster.helper.FormBuildHelper;
import me.riddhimanadib.formmaster.model.FormElement;
import me.riddhimanadib.formmaster.model.FormObject;


public class StaffFilterActivity extends AppCompatActivity {
    private static final String TAG = "StaffFilterActivity";

    private RecyclerView mRecyclerView;
    private FormBuildHelper mFormBuilder;

    private FormElement mNameField;
    private FormElement mDepartmentField;
    private FormElement mTimeField;
    private FormElement mLateField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_filter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.filter_form);
        mFormBuilder = new FormBuildHelper(this, mRecyclerView);
        Button submitBtn = (Button) findViewById(R.id.button_submit);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "submitBtn onClick");

                Intent resultIntent = new Intent();

                String name = mNameField.getValue();
                String department = mDepartmentField.getValue();
                String time = mTimeField.getValue();
                String late = mLateField.getValue();

                Log.d(TAG, "name: " + name);
                Log.d(TAG, "department:" + department);
                Log.d(TAG, "time: " + time);
                Log.d(TAG, "late: " + late);

                resultIntent.putExtra(MainFragment.STAFF_FILTER_NAME, name);
                resultIntent.putExtra(MainFragment.STAFF_FILTER_DEPARTMENT, department);
                resultIntent.putExtra(MainFragment.STAFF_FILTER_TIME, time);
                resultIntent.putExtra(MainFragment.STAFF_FILTER_LATE, late);

                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

        setupForm();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupForm() {
        mNameField = FormElement.createInstance().setType(FormElement.TYPE_EDITTEXT_TEXT_SINGLELINE).setTitle("员工姓名");

        ArrayList<String> departments = new ArrayList<>(Arrays.asList(Constants.DEPARTMENT));
        mDepartmentField = FormElement.createInstance().setType(FormElement.TYPE_SPINNER_DROPDOWN).setTitle("部门").setOptions(departments);
        mTimeField = FormElement.createInstance().setType(FormElement.TYPE_PICKER_TIME).setTitle("签到时间");

        List<String> late = new ArrayList<>();
        late.add("是");
        late.add("否");
        mLateField = FormElement.createInstance().setType(FormElement.TYPE_SPINNER_DROPDOWN).setTitle("迟到").setOptions(late);

        List<FormObject> formItems = new ArrayList<>();
        formItems.add(mNameField);
        formItems.add(mDepartmentField);
        formItems.add(mTimeField);
        formItems.add(mLateField);

        mFormBuilder.addFormElements(formItems);
        mFormBuilder.refreshView();
    }
}
