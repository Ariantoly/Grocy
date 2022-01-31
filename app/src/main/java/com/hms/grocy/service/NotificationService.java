package com.hms.grocy.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.hms.grocy.R;
import com.hms.grocy.SplashActivity;
import com.hms.grocy.database.DatabaseHelper;
import com.hms.grocy.model.GrocyNotification;
import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.RemoteMessage;

public class NotificationService extends HmsMessageService {

    @Override
    public void onNewToken(String token) {
        // Obtain a push token.
        Log.i("Grocy", "have received refresh token " + token);

        // Check whether the token is null.
        if (!TextUtils.isEmpty(token)) {
            refreshedTokenToServer(token);
        }
    }

    private void refreshedTokenToServer(String token) {
        Log.i("Grocy", "sending token to server. token:" + token);
    }

    @Override
    public void onMessageReceived(RemoteMessage message) {

        // Check whether the message is empty.
        if (message == null) {
            Log.v("Grocy", "Received message entity is null!");
            return;
        }

        showNotification(message);
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        dbHelper.addNotification(new GrocyNotification(message.getNotification().getTitle(), message.getNotification().getBody()));
        Log.v("Grocy", "Loss");
        Boolean judgeWhetherIn10s = false;
        // If the message is not processed within 10 seconds, create a job to process it.
        if (judgeWhetherIn10s) {
            startWorkManagerJob(message);
        } else {
            // Process the message within 10 seconds.
            processWithin10s(message);
        }
    }
    private void startWorkManagerJob(RemoteMessage message) {
        Log.v("Grocy", "Start new job processing.");
    }
    private void processWithin10s(RemoteMessage message) {
        Log.v("Grocy", "Processing now.");
    }

    private void showNotification(RemoteMessage message) {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "N001")
                .setSmallIcon(R.drawable.ic_grocy)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setContentTitle(message.getNotification().getTitle())
                .setContentText(message.getNotification().getBody())
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("N001", "Notification", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(0, builder.build());
    }
}
