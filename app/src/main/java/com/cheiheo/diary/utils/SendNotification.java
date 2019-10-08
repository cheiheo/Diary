package com.cheiheo.diary.utils;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.cheiheo.diary.R;

/**
 * author:chen hao
 * email::
 * time:2019/09/27
 * desc:
 * version:1.0
 */

public class SendNotification {

    public static final int NOTIFICATION_ID = 1;
    public static final String CHANNEL_ID = "diary_channel_id";
    public static final String CHANNEL_NAME = "diary_channel";
    public static final String CHANNEL_DESCRIPTION = "diary_notification";
    public static final int CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;
    private final NotificationManager manager;
    private NotificationChannel channel;
    private Notification notification;
    private Context context;

    public SendNotification(Context context) {
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        this.context = context;
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, CHANNEL_IMPORTANCE);
        channel.setDescription(CHANNEL_DESCRIPTION);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        manager.createNotificationChannel(channel);
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotification(String title, String content) {
        notification = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(content)
                .setChannelId(CHANNEL_ID)
                .setColor(Color.YELLOW)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .build();
    }

    private void createNotificationLowVersion(String title, String content) {
        notification = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(content)
                .setColor(Color.YELLOW)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .build();
    }

    public void send(String title, String content) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
            createNotification(title, content);
            manager.notify(NOTIFICATION_ID, notification);
        } else {
            createNotificationLowVersion(title, content);
            manager.notify(NOTIFICATION_ID, notification);
        }
    }
}
