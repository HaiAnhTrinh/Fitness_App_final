package com.example.troyphattrinh.fitness_app;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

public class Noti {
    public static void showNotification(FootstepActivity context) {
        android.app.Notification notification = new NotificationCompat.Builder(context.getContext())
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("10000 steps!!!")
                .setContentTitle("Fitness app")
                .setContentText("You have already walked 10000 steps.")
                .setWhen(System.currentTimeMillis())
                .setPriority(android.app.Notification.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setOngoing(false)
                .setDefaults(android.app.Notification.DEFAULT_VIBRATE | android.app.Notification.DEFAULT_SOUND)
                .setContentIntent(PendingIntent.getActivity(context.getContext(), 1, new Intent(context.getContext(), FootstepActivity.class), PendingIntent.FLAG_CANCEL_CURRENT))
                .build();
        NotificationManager notificationManager = (NotificationManager) context.getActivity().getSystemService(context.getActivity().NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}
