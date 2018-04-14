package com.nikitagordia.chatme.helper.firebasenotify;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
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
import com.nikitagordia.chatme.utils.Const;
import com.nikitagordia.chatme.utils.ImageUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

/**
 * Created by nikitagordia on 4/10/18.
 */

public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(remoteMessage.getData().get("owner_id"))) return;

        try {
            final Bitmap bitmap = Picasso.with(getApplicationContext()).load(remoteMessage.getData().get("owner_photo_url")).resize(ImageUtils.SIZE_M, ImageUtils.SIZE_M).get();
            new Handler(getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    final Notification notification = new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.drawable.user_photo_holder)
                            .setContentTitle(remoteMessage.getData().get("owner_nickname"))
                            .setContentText(remoteMessage.getData().get("content"))
                            .setSmallIcon(R.drawable.icon_message_notification)
                            .setLargeIcon(bitmap)
                            .build();
                    ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(0, notification);
                }
            });
        } catch (IOException e) {
            Log.d("mytg", "Error " + e.getMessage());
        }
    }

    public boolean isForeground(String myPackage) {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfo = manager.getRunningTasks(1);
        ComponentName componentInfo = runningTaskInfo.get(0).topActivity;
        return componentInfo.getPackageName().equals(myPackage);
    }
}

// cH-vsPJxgCo:APA91bHxIezZXwh6lXieE108dPBTUNsrlHqswneYGyiLdHj2kBBc3FfVTcKtrPwtrJ1VbZpQ4QvlBy-zP3xQROwQVRNy1Sx6riDxPpNUSjYrNBGQdHEbGuQgj3oIpgtROe3H8qPOLXMg