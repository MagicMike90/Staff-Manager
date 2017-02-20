package com.sanitation.app;

import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Michael on 2/17/17.
 */

public class Utils {
    private static final String TAG = "Utils";
    private static Utils mInstance;
    private static Context mCtx;

    private Utils(Context context) {
        mCtx = context;
    }
    public static synchronized Utils getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Utils(context);
        }
        return mInstance;
    }
    public String getName(String name) {
        return name;
    }
    public String getGender(String gender) {
        return gender == "male" ? "男": "女";
    }
    public String getDateStr(String date) {
        String mm = date.replaceAll("[^-?0-9]+", "");
        String pattern = "MM/dd/yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(mm));
        return  formatter.format(calendar.getTime());
    }

}
