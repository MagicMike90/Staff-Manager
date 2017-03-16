package com.sanitation.app;

import android.content.Context;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.db.memory.InMemoryDatabase;

/**
 * Created by Michael on 3/8/17.
 */
public class MeteorDDP {
    private static MeteorDDP mInstance;
    private Meteor mMeteor;

    public static synchronized MeteorDDP getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MeteorDDP(context);
        }
        return mInstance;
    }

    public Meteor getConnection(){
        return mMeteor;
    }


    private MeteorDDP(Context context) {
        Meteor.setLoggingEnabled(true);;
        mMeteor = new Meteor(context, Constants.METEOR_SERVER_SOCKET,new InMemoryDatabase());
    }
}
