package com.webianks.exp.crimson.sleep_better;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyAlarmReciever extends BroadcastReceiver {

    public static final int REQUEST_CODE = 12345;

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent notifIntent = new Intent(context,NotificationService.class);
        context.startService(notifIntent);
    }
}

