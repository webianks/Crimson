package com.webianks.exp.crimson;

import android.app.Application;
import android.content.Context;


public class CrimsonApplication extends Application {

    private static CrimsonApplication instance;

    public static CrimsonApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance.getApplicationContext();

    }

    @Override
    public void onCreate() {

        super.onCreate();
        instance = this;
    }
}
