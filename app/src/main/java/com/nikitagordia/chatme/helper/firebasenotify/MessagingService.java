package com.nikitagordia.chatme.helper.firebasenotify;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nikitagordia.chatme.R;
import com.nikitagordia.chatme.module.chat.view.ChatActivity;
import com.nikitagordia.chatme.utils.Const;
import com.nikitagordia.chatme.utils.ImageUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

/**
 * Created by nikitagordia on 4/10/18.
 */

public class MessagingService extends FirebaseMessagingService {

    private static final String TAG = "MessagingService";

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("mytg", "HERE");
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(remoteMessage.getData().get("owner_id"))) return;

        try {
            final Bitmap bitmap = Picasso.with(getApplicationContext()).load(remoteMessage.getData().get("owner_photo_url")).placeholder(R.drawable.user_photo_holder).resize(ImageUtils.SIZE_M, ImageUtils.SIZE_M).get();
            new Handler(getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    final Notification notification = new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.drawable.user_photo_holder)
                            .setContentTitle(remoteMessage.getData().get("owner_nickname"))
                            .setContentText(remoteMessage.getData().get("content"))
                            .setSmallIcon(R.drawable.icon_message_notification)
                            .setLargeIcon(bitmap)
                            .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, ChatActivity.getIntent(remoteMessage.getData().get("chat_id"), getApplicationContext()), 0))
                            .build();
                    notification.flags |= Notification.FLAG_AUTO_CANCEL;
                    ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(0, notification);
                }
            });
        } catch (IOException e) {
            Log.d(TAG, "Error " + e.getMessage());
        }
    }
}

// cH-vsPJxgCo:APA91bHxIezZXwh6lXieE108dPBTUNsrlHqswneYGyiLdHj2kBBc3FfVTcKtrPwtrJ1VbZpQ4QvlBy-zP3xQROwQVRNy1Sx6riDxPpNUSjYrNBGQdHEbGuQgj3oIpgtROe3H8qPOLXMg