package com.nikitagordia.chatme.helper.firebasenotify;

import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by nikitagordia on 4/10/18.
 */

public class InstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
    }
}
