package com.sanitation.app.StaffManagement.staffmanagement.sign;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.sanitation.app.Constants;
import com.sanitation.app.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.riddhimanadib.formmaster.helper.FormBuildHelper;
import me.riddhimanadib.formmaster.model.FormElement;
import me.riddhimanadib.formmaster.model.FormObject;

public class StaffSignInAndOutActivity extends AppCompatActivity {

    public static String CHECK_IN_AN_OUT_TYPE = "type";
    private RecyclerView mRecyclerView;
    private FormBuildHelper mFormBuilder;
    private List<FormObject> formItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_sign_in_and_out);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            int title = getIntent().getIntExtra(CHECK_IN_AN_OUT_TYPE, 0);
            setTitle(title);
        }

        initForm();
    }

    private void initForm() {
        // initialize variables
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mFormBuilder = new FormBuildHelper(this, mRecyclerView);

        ArrayList<String> departments = new ArrayList<>(Arrays.asList(Constants.DEPARTMENT));
        FormElement field_department = FormElement.createInstance().setType(FormElement.TYPE_SPINNER_DROPDOWN).setTitle("部门").setOptions(departments);
        FormElement element11 = FormElement.createInstance().setType(FormElement.TYPE_EDITTEXT_TEXT_SINGLELINE).setTitle("员工号码");
        FormElement date = FormElement.createInstance().setType(FormElement.TYPE_PICKER_DATE).setTitle("签到／签退日期");
        FormElement time = FormElement.createInstance().setType(FormElement.TYPE_PICKER_TIME).setTitle("签到／签退时间");
        FormElement location = FormElement.createInstance().setType(FormElement.TYPE_EDITTEXT_TEXT_SINGLELINE).setTitle("签到／签退地点");

        formItems.add(field_department);
        formItems.add(element11);
        formItems.add(date);
        formItems.add(time);
        formItems.add(location);



        mFormBuilder.addFormElements(formItems);
        mFormBuilder.refreshView();
    }


//    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_staff_sign_in_and_out;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, StaffSignInAndOutActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
