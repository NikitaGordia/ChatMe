package com.nikitagordia.chatme.helper.firebasenotify;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by nikitagordia on 4/10/18.
 */

public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("mytg", "Hello " + remoteMessage.getData().toString());
    }
}

// cH-vsPJxgCo:APA91bHxIezZXwh6lXieE108dPBTUNsrlHqswneYGyiLdHj2kBBc3FfVTcKtrPwtrJ1VbZpQ4QvlBy-zP3xQROwQVRNy1Sx6riDxPpNUSjYrNBGQdHEbGuQgj3oIpgtROe3H8qPOLXMg