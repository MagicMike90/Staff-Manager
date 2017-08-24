package com.sanitation.app.Main.Sign.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sanitation.app.Constants;
import com.sanitation.app.Login.StaffInfo;
import com.sanitation.app.Main.Sign.step.StepInfoStorage;
import com.sanitation.app.R;
import com.sanitation.app.Utils.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorSingleton;
import im.delight.android.ddp.ResultListener;
import me.riddhimanadib.formmaster.helper.FormBuildHelper;
import me.riddhimanadib.formmaster.model.FormElement;
import me.riddhimanadib.formmaster.model.FormObject;

public class StaffSignInAndOutActivity extends AppCompatActivity {
    private static final String TAG = "StaffSignInAndOutActivity";

    public static String CHECK_IN_AN_OUT_TYPE = "type";
    private RecyclerView mRecyclerView;
    private FormBuildHelper mFormBuilder;
    private List<FormObject> formItems = new ArrayList<>();

    private Button mConfirmBtn;
    private static final int TAG_TYPE = 1;
    private static final int TAG_DEPARTMENT = 2;
    private static final int TAG_STAFF = 3;
    private static final int TAG_DATE = 4;
    private static final int TAG_TIME = 5;
    private static final int TAG_LOCATION = 6;

    private Meteor mMeteor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_sign_in_and_out);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            setTitle(R.string.sign_action);
        }


        mMeteor = MeteorSingleton.getInstance();

        mConfirmBtn = (Button) findViewById(R.id.button_confirm);
        mConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FormElement type = mFormBuilder.getFormElement(TAG_TYPE);
                FormElement department = mFormBuilder.getFormElement(TAG_DEPARTMENT);
                FormElement staff = mFormBuilder.getFormElement(TAG_STAFF);
                FormElement date = mFormBuilder.getFormElement(TAG_DATE);
                FormElement time = mFormBuilder.getFormElement(TAG_TIME);
                FormElement location = mFormBuilder.getFormElement(TAG_LOCATION);

                Map<String, Object> sign = new HashMap<String, Object>();
                sign.put("id", mMeteor.getUserId());
                sign.put("type",type.getValue());
                sign.put("staff_id", staff.getValue());
                sign.put("department", department.getValue());


                sign.put("date", Utils.getInstance(StaffSignInAndOutActivity.this).convertDate(date.getValue()));
                sign.put("time", time.getValue());
                sign.put("latitude", StepInfoStorage.getInstance().latitude);
                sign.put("longitude", StepInfoStorage.getInstance().longitude);

                Object[] queryParams = {sign};


                mMeteor.call("signInOut.mobile_insert",
                        new Object[]{sign},
                        new ResultListener() {
                            @Override
                            public void onSuccess(String result) {
                                Toast.makeText(StaffSignInAndOutActivity.this, "签到／签退成功: " + result, Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onError(String error, String reason, String details) {
                                Toast.makeText(StaffSignInAndOutActivity.this, "Error: " + error + " " + reason + " " + details, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        initForm();
    }

    private void initForm() {
        // initialize variables
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mFormBuilder = new FormBuildHelper(this, mRecyclerView);

        ArrayList<String> types = new ArrayList<>(Arrays.asList(Constants.SIGN_TYPES));
        FormElement field_types = FormElement.createInstance().setTag(TAG_TYPE).setType(FormElement.TYPE_SPINNER_DROPDOWN).setTitle("签到类型").setOptions(types);

        ArrayList<String> departments = new ArrayList<>(Arrays.asList(Constants.DEPARTMENT));
        FormElement field_department = FormElement.createInstance().setTag(TAG_DEPARTMENT).setType(FormElement.TYPE_SPINNER_DROPDOWN).setTitle("部门").setOptions(departments);

        FormElement staff_id = FormElement.createInstance().setTag(TAG_STAFF).setType(FormElement.TYPE_EDITTEXT_TEXT_SINGLELINE).setTitle("员工号码");
        FormElement date = FormElement.createInstance().setTag(TAG_DATE).setType(FormElement.TYPE_PICKER_DATE).setTitle("签到／签退日期");
        FormElement time = FormElement.createInstance().setTag(TAG_TIME).setType(FormElement.TYPE_PICKER_TIME).setTitle("签到／签退时间");
        FormElement location = FormElement.createInstance().setTag(TAG_LOCATION).setType(FormElement.TYPE_EDITTEXT_TEXT_SINGLELINE).setTitle("签到／签退地点");

        formItems.add(field_types);
        formItems.add(field_department);
        formItems.add(staff_id);
        formItems.add(date);
        formItems.add(time);
        formItems.add(location);

        mFormBuilder.addFormElements(formItems);
        mFormBuilder.refreshView();
    }


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
