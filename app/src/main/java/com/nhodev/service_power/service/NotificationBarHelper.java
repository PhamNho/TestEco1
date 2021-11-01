package com.nhodev.service_power.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.nhodev.service_power.MainActivity;
import com.nhodev.service_power.R;
import com.nhodev.service_power.utils.NotificationConst;


public class NotificationBarHelper {
    private Context context;

    public NotificationBarHelper(Context context) {
        this.context = context;
    }

    Notification getNotification(String musicName) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_notification);
        remoteViews.setTextViewText(R.id.tvTitleNotification, musicName);

        // tạo notification
        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context,
                NotificationConst.FOREGROUND_PENDING_INTENT_RQ_CODE,
                resultIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = new NotificationCompat.Builder(context, "channel_id_1")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setAutoCancel(false)
                .setOngoing(true)
                .setCustomContentView(remoteViews)
                .setContentIntent(resultPendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setVibrate(null)
                .build();
        notification.flags = Notification.FLAG_ONGOING_EVENT;

        return notification;
    }

    public void updateNotification(String musicName) {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        Notification notification = getNotification(musicName);
        notificationManagerCompat.notify(NotificationConst.FOREGROUND_RQ_CODE, notification);
    }
}
