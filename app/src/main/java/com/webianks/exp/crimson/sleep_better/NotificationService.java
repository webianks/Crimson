package com.webianks.exp.crimson.sleep_better;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.webianks.exp.crimson.R;


public class NotificationService extends IntentService {


    public NotificationService() {
        super("notification");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        screenAlertMessage(getApplicationContext(),"Take a break.");
    }

    private void screenAlertMessage(Context context, String msg) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Relax your eyes a little bit.")
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setVibrate(new long[0])
                        .setAutoCancel(true)
                        .setContentText(msg);

        int mNotificationId = 001;

        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

    }
}
