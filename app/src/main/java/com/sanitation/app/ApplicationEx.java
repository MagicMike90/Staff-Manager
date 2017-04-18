package com.sanitation.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;

import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo;

/**
 * Created by Michael on 4/18/17.
 */

public class ApplicationEx extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RxPaparazzo.register(this);
    }
}
