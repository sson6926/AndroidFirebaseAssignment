package com.shawningx.week10.fcm;

import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.shawningx.week10.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String title = null;
        String body = null;

        if (remoteMessage.getNotification() != null) {
            title = remoteMessage.getNotification().getTitle();
            body = remoteMessage.getNotification().getBody();
        }

        if (TextUtils.isEmpty(title)) {
            title = getString(R.string.notification_default_title);
        }
        if (TextUtils.isEmpty(body)) {
            body = getString(R.string.notification_default_body);
        }

        NotificationHelper.showNotification(this, title, body);
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(TAG, "New token: " + token);
        new FcmTokenRepository().updateToken(token);
    }
}
