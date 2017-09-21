package com.sanitation.app.Login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sanitation.app.Constants;
import com.sanitation.app.Main.activities.MainActivity;
import com.sanitation.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.MeteorSingleton;
import im.delight.android.ddp.ResultListener;
import im.delight.android.ddp.db.memory.InMemoryDatabase;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements MeteorCallback {
    private static final String TAG = "LoginActivity";


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private Meteor mMeteor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);


        Meteor.setLoggingEnabled(true);

        if (!MeteorSingleton.hasInstance())
            MeteorSingleton.createInstance(this, Constants.METEOR_SERVER_SOCKET, new InMemoryDatabase());
        mMeteor = MeteorSingleton.getInstance();
//        mMeteor = new Meteor(this, Constants.METEOR_SERVER_SOCKET, new InMemoryDatabase());
        mMeteor.addCallback(this);
        mMeteor.connect();
        showProgress(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!mMeteor.isConnected())mMeteor.connect();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);


        boolean cancel = false;
        View focusView = null;

//        // Check for a valid password, if the user entered one.
//        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
//            mPasswordView.setError(getString(R.string.error_invalid_password));
//            focusView = mPasswordView;
//            cancel = true;
//        }

//        // Check for a valid email address.
//        if (TextUtils.isEmpty(email)) {
//            mEmailView.setError(getString(R.string.error_field_required));
//            focusView = mEmailView;
//            cancel = true;
//        } else if (!isEmailValid(email)) {
//            mEmailView.setError(getString(R.string.error_invalid_email));
//            focusView = mEmailView;
//            cancel = true;
//        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            String email = mEmailView.getText().toString();
            String password = mPasswordView.getText().toString();

            email = "michael";
            password = "password";

            mMeteor.loginWithUsername(email, password, new ResultListener() {
                @Override
                public void onSuccess(String result) {
                    Log.d(TAG, result);
                    showProgress(false);


                    Log.d(TAG, "Logged in: " + result);

                    try {
                        JSONObject login = new JSONObject(result);

                        String userId = login.getString("id");
                        String token = login.getString("token");
                        long expiry = login.getJSONObject("tokenExpires").getLong("$date");

                        Map<String, Object> user = new HashMap<String, Object>();
                        user.put("_id", userId);

                        Object[] queryParams = {user};

                        mMeteor.call("user.findStaffId", queryParams, new ResultListener() {
                            @Override
                            public void onSuccess(String result) {
                                Log.d(TAG, "Call result: " + result);
                            }

                            @Override
                            public void onError(String error, String reason, String details) {
                                Log.d(TAG, "Error: " + error + " " + reason + " " + details);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onError(String s, String s1, String s2) {
                    Log.e(TAG, s + " -> " + s1 + " -> " + s2);
                    showProgress(false);
                }
            });

//            mAuthTask = new UserLoginTask(email, password);
//            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
//        return email.contains("@");
        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
//        return password.length() > 4;
        return true;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    public void onDestroy() {
//        mMeteor.disconnect();
//        mMeteor.removeCallback(this);
        super.onDestroy();
        Log.d(TAG, "onDestroy");

    }

    @Override
    public void onConnect(boolean signedInAutomatically) {
        Log.d(TAG, "onConnect");

        String subscriptionId = mMeteor.subscribe("users");
        if (signedInAutomatically) {
            // Store values at the time of the login attempt.

            mMeteor.call("user.findStaffId", new Object[]{mMeteor.getUserId()}, new ResultListener() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONObject staff = new JSONObject(result);
                        StaffInfo.getInstance().id = mMeteor.getUserId();
                        StaffInfo.getInstance().staff_name = staff.getString("staff_name");
                        StaffInfo.getInstance().staff_id = staff.getString("staff_id");
                        StaffInfo.getInstance().role = staff.getString("role");
                        StaffInfo.getInstance().department = staff.getString("department");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "Call result: " + result);
                    //start location tracker
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onError(String error, String reason, String details) {
                    Log.d(TAG, "Error: " + error + " " + reason + " " + details);
                }
            });



        }
        showProgress(false);
    }

    @Override
    public void onDisconnect() {
        Log.d(TAG, "onDisconnect");
    }

    @Override
    public void onException(Exception e) {
        Log.d(TAG, "onException： " + e.getMessage());
        Log.d(TAG, Log.getStackTraceString(e));
    }

    @Override
    public void onDataAdded(String collectionName, String documentID, String newValuesJson) {

    }

    @Override
    public void onDataChanged(String collectionName, String documentID, String updatedValuesJson, String removedValuesJson) {

    }

    @Override
    public void onDataRemoved(String collectionName, String documentID) {

    }
}

