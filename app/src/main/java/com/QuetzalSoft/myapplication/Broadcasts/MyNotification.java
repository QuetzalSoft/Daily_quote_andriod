package com.QuetzalSoft.myapplication.Broadcasts;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.QuetzalSoft.myapplication.R;

public class MyNotification extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("ME", "Notification started");

        if(intent.getAction().equals("ok")) {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context,"notify")
                            .setSmallIcon(R.drawable.dailythought)
                            .setContentTitle("My notification")
                            .setContentText("Hello World!");

            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(1, mBuilder.build());
        }
    }
}
