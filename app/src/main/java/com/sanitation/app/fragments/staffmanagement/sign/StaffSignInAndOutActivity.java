package com.sanitation.app.fragments.staffmanagement.sign;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.sanitation.app.R;
import com.sanitation.app.fragments.staffmanagement.sign.step.AbstractStepperActivity;

public class StaffSignInAndOutActivity extends AbstractStepperActivity {

    public static String CHECK_IN_AN_OUT_TYPE = "type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_staff_sign_in_and_out);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            int title = getIntent().getIntExtra(CHECK_IN_AN_OUT_TYPE,0);
            setTitle(title);
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_staff_sign_in_and_out;
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
