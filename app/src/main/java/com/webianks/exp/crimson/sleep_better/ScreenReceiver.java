package com.webianks.exp.crimson.sleep_better;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class ScreenReceiver extends BroadcastReceiver {

    public static boolean wasScreenOn = true;
    private long tStart;
    private long tEnd;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {

            wasScreenOn = false;

            tEnd = System.currentTimeMillis();
            long tDelta = tEnd - tStart;
            double elapsedSeconds = tDelta / 1000.0;

            cancelAlarm(context);


        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {

            wasScreenOn = true;
            tStart = System.currentTimeMillis();

            scheduleAlarm(context);

        }
    }

    public void scheduleAlarm(Context context) {

        Intent intent = new Intent(context, MyAlarmReciever.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(context, MyAlarmReciever.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.set(AlarmManager.RTC_WAKEUP, firstMillis+5000, pIntent);

    }


    public static void scheduleAlarmNow(Context context) {

        Intent intent = new Intent(context, MyAlarmReciever.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(context, MyAlarmReciever.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.set(AlarmManager.RTC_WAKEUP, firstMillis+(7200000), pIntent);

    }



    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, MyAlarmReciever.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(context, MyAlarmReciever.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pIntent);
    }

}
