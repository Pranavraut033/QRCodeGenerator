package com.preons.pranav.QRCodeGenerator.utils;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import static com.preons.pranav.QRCodeGenerator.utils.c.TAG;

public class NotificationServices extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Map<String, String> a = remoteMessage.getData();
            if (a.containsKey("update")) {
                Intent intent = new Intent(getApplicationContext(), UpdateDialog.class);
                SharedPreferences sharedPreferences = getSharedPreferences(c.DSP, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("version", Integer.parseInt(a.get("version")));
                RemoteMessage.Notification notification = remoteMessage.getNotification();
                if (notification != null) {
                    editor.putString("title", notification.getTitle());
                    editor.putString("body", notification.getBody() + "\n" + "Change Log: " + a.get("log"));
                }
                editor.apply();
                intent.putExtra("type", "update");
                startActivity(intent);
            }
        }
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
        Log.d(TAG, "onDeletedMessages: ");
    }
}
