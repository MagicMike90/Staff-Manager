package com.sanitation.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.sanitation.app.notice.NoticeActivity;
import com.sanitation.app.fragments.eventmanagement.EventListFragment;
import com.sanitation.app.fragments.weather.WeatherFragment;
import com.sanitation.app.service.GPSService;

import com.sanitation.app.fragments.assessment.AssessmentActivity;
import com.sanitation.app.staff.StaffActivity;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorSingleton;
import im.delight.android.ddp.ResultListener;
import im.delight.android.ddp.db.memory.InMemoryDatabase;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";

    private NavigationView mNavigationView;
    private Fragment mCurrentFragment;

    private Utils mUtils;
    private Meteor mMeteor;

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


        init();

    }

    private void init() {
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);


        if (!MeteorSingleton.hasInstance())
            MeteorSingleton.createInstance(this, Constants.METEOR_SERVER_SOCKET, new InMemoryDatabase());
        //start location tracker
        Intent ddpIntent = new Intent(this, GPSService.class);
        ddpIntent.putExtra("userId", MeteorSingleton.getInstance().getUserId());
        startService(ddpIntent);


//        MenuItem item = mNavigationView.getMenu().getItem(0);
//        onNavigationItemSelected(item);
        Fragment weatherFrag = new WeatherFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, weatherFrag).commit();

        mUtils = Utils.getInstance(this);
        mMeteor = MeteorSingleton.getInstance();
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

            Intent intent = new Intent(getApplicationContext(), NoticeActivity.class);
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
            MeteorSingleton.getInstance().logout(new ResultListener() {
                @Override
                public void onSuccess(String result) {
                    Log.d(TAG, "Logout Successfully");
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mCurrentFragment instanceof MainFragment) {
            ((MainFragment) mCurrentFragment).onActivityResult(requestCode, resultCode, data);
        }
    }
}
