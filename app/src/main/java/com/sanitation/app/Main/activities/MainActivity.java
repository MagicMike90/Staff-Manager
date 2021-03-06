package com.sanitation.app.Main.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.franmontiel.fullscreendialog.FullScreenDialogFragment;
import com.sanitation.app.Assessment.activities.AssessmentActivity;
import com.sanitation.app.Event.fragments.EventListFragment;
import com.sanitation.app.Login.LoginActivity;
import com.sanitation.app.Main.fragments.MainFragment;
import com.sanitation.app.Main.interfaces.OnOptionSelectedListener;
import com.sanitation.app.Notice.activities.NoticeListActivity;
import com.sanitation.app.R;
import com.sanitation.app.Services.GPSService;
import com.sanitation.app.Main.Sign.activities.StaffSignInAndOutActivity;
import com.sanitation.app.StaffManagement.activities.StaffActivity;

import im.delight.android.ddp.MeteorSingleton;
import im.delight.android.ddp.ResultListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnOptionSelectedListener, FullScreenDialogFragment.OnConfirmListener,
        FullScreenDialogFragment.OnDiscardListener {
    private static final String TAG = "MainActivity";

    private NavigationView mNavigationView;
    private Fragment mCurrentFragment;
    private FullScreenDialogFragment mSignActionFragment;
    private static final String dialogTag = "signActionFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final String dialogTag = "dialog";
        if (savedInstanceState != null) {
            mSignActionFragment = (FullScreenDialogFragment) getSupportFragmentManager().findFragmentByTag(dialogTag);
            if (mSignActionFragment != null) {
                mSignActionFragment.setOnConfirmListener(this);
                mSignActionFragment.setOnDiscardListener(this);
            }
        }

        init();

    }

    private void init() {
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);


//        if (!MeteorSingleton.hasInstance())
//            MeteorSingleton.createInstance(this, Constants.METEOR_SERVER_SOCKET, new InMemoryDatabase());

        //start location tracker
        Intent ddpIntent = new Intent(this, GPSService.class);
        ddpIntent.putExtra("userId", MeteorSingleton.getInstance().getUserId());
        startService(ddpIntent);


//        MenuItem item = mNavigationView.getMenu().getItem(0);
//        onNavigationItemSelected(item);
        Fragment mainFrag = new MainFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, mainFrag).commit();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Class fragmentClass = MainFragment.class;
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_notice) {
//            fragmentClass = NoticeListFragment.class;

            Intent intent = new Intent(getApplicationContext(), NoticeListActivity.class);
            startActivity(intent);

        }
        if (id == R.id.nav_staff) {
            Intent intent = new Intent(getApplicationContext(), StaffActivity.class);
            startActivity(intent);
        }
        if (id == R.id.nav_camera) {
            // Handle the camera action
//            Intent externalActivityIntent = new Intent(this, StaffMainActivity.class);
//            startActivity(externalActivityIntent);
            fragmentClass = EventListFragment.class;

//        } else if (id == R.id.nav_gallery) {
////            Intent externalActivityIntent = new Intent(this, StaffListActivity.class);
////            startActivity(externalActivityIntent);
//
//
//        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(MainActivity.this, AssessmentActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {


        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_exit) {
            Log.d(TAG, "EXIT");
            MeteorSingleton.getInstance().logout(new ResultListener() {
                @Override
                public void onSuccess(String result) {
                    Log.d(TAG, "Logout Successfully");
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onError(String error, String reason, String details) {
                    Log.d(TAG, "Logout Error");
                }
            });
        }

        try {
            mCurrentFragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.fragment_container, mCurrentFragment).commit();
        // Highlight the selected item has been done by NavigationView
        item.setChecked(true);
//        setTitle(item.getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (mCurrentFragment instanceof MainFragment) {
//            ((MainFragment) mCurrentFragment).onActivityResult(requestCode, resultCode, data);
//        }
//    }

    @Override
    public void onOptionSelected(int id) {
        Intent intent = new Intent();
        switch (id) {
            case R.id.staff_sign:
                intent.setClass(this, StaffSignInAndOutActivity.class);
                startActivity(intent);
//                final Bundle args = new Bundle();
//                args.putString(SignActionFragment.EXTRA_NAME, "");
//                mSignActionFragment = new FullScreenDialogFragment.Builder(MainActivity.this)
//                        .setTitle(R.string.sign_action)
//                        .setConfirmButton(R.string.sign_dialog_positive_action)
//                        .setOnConfirmListener(MainActivity.this)
//                        .setOnDiscardListener(MainActivity.this)
//                        .setContent(SignActionFragment.class,args)
//                        .build();
//
//                mSignActionFragment.show(getSupportFragmentManager(), dialogTag);
                break;
            case R.id.staff_sign_history:
                intent.setClass(this, StaffSignInAndOutActivity.class);
//                Intent intent = new Intent(this, StaffSignInAndOutActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onConfirm(@Nullable Bundle result) {

    }

    @Override
    public void onDiscard() {

    }
}
