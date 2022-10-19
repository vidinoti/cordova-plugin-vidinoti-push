package com.vidinoti.cordova;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.media.RingtoneManager;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.vidinoti.android.vdarsdk.VDARSDKController;

import java.util.Map;

/**
 * Service that receives the push notifications.
 * When a new notification is received, it creates a new notification in the status panel.
 * When the notification is opened, it opens the start activity and some information needs to
 * be forwarded to the Vidinoti SDK. See the documentation at https://vidinoti.github.io/ for
 * more information.
 */
public class VidinotiFcmListenerService extends FirebaseMessagingService {

    private static final String TAG = VidinotiFcmListenerService.class.getName();

    private static final String CHANNEL_ID = "vidinoti_push_channel";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            Log.w(TAG, "NotificationManager is null");
            return;
        }

        String nid = data.get("nid");
        String message = data.get("message");
        if (nid == null || nid.length() == 0 || message == null || message.length() == 0) {
            Log.w(TAG, "Invalid push data");
            return;
        }

        // Get the default intent of the app
        Intent appIntent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        if (appIntent == null) {
            Log.w(TAG, "Launch intent not found");
            return;
        }
        appIntent.putExtra("nid", nid);
        appIntent.putExtra("remote", true);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, appIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        ApplicationInfo ai = getApplicationInfo();
        createNotificationChannel(notificationManager);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(ai.icon!=0 ? ai.icon : android.R.drawable.star_big_off)
                .setContentTitle(getString(ai.labelRes))
                .setContentText(message)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        //To make sure notification ID is unique over time
        int when = (int) ((System.currentTimeMillis() - 1419120000000L) / 1000L);

        notificationManager.notify(when, mBuilder.build());
    }

    /**
     * Creates a channel if necessary (required from Android API 26)
     */
    private void createNotificationChannel(NotificationManager notificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "App";
            String description = "Notification about latest news";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        // Forward the token to the Vidinoti SDK
        VDARSDKController controller = VDARSDKController.getInstance();
        if (controller != null) {
            controller.updatePushNotificationToken(token);
        }
    }
}