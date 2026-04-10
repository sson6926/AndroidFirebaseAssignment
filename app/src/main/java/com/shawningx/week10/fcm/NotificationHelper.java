package com.shawningx.week10.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.shawningx.week10.R;

public class NotificationHelper {
    public static final String CHANNEL_ID = "movie_reminder";

    private NotificationHelper() {
    }

    public static void ensureChannel(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }

        String name = context.getString(R.string.notification_channel_name);
        String description = context.getString(R.string.notification_channel_desc);
        NotificationChannel channel = new NotificationChannel(
            CHANNEL_ID,
            name,
            NotificationManager.IMPORTANCE_DEFAULT
        );
        channel.setDescription(description);

        NotificationManager manager = context.getSystemService(NotificationManager.class);
        if (manager != null) {
            manager.createNotificationChannel(channel);
        }
    }

    public static void showNotification(Context context, String title, String body) {
        ensureChannel(context);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager manager =
            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify((int) System.currentTimeMillis(), builder.build());
        }
    }
}
