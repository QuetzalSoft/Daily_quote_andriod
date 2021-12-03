package com.QuetzalSoft.myapplication.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentTransaction;

import com.QuetzalSoft.myapplication.Login;
import com.QuetzalSoft.myapplication.MainActivity;
import com.QuetzalSoft.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class NotifyService extends Service {

int delay =1000;
public static int show_noti;
    public NotifyService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onTaskRemoved(intent);

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                Log.e("time", "onStartCommand: " + currentTime);
                if (currentTime.equals("12:30:00")) {
                    show_notification();
                }
            }
        }, 0, delay);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
    }

    void show_notification(){
            if(show_noti ==1) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    AudioAttributes audioAttributes = new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .build();


                    NotificationChannel channel = new NotificationChannel("1", "1", NotificationManager.IMPORTANCE_DEFAULT);
                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);
                }

                Intent activityIntent = new Intent(getApplicationContext(), Login.class);
                PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(),
                        0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(getApplicationContext(), "1")
                                .setSmallIcon(R.drawable.dailythought)
                                .setContentTitle("The Daily Thought")
                                .setContentText("There is a new quote to motivate you.")
                                .setColor(getResources().getColor(R.color.colorPrimary))
                                .setContentIntent(contentIntent)
                                .setSound(soundUri)
                                .setAutoCancel(true);

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
                managerCompat.notify(111, builder.build());

            }
    }
}
