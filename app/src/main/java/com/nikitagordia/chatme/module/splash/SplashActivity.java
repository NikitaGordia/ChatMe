package com.nikitagordia.chatme.module.splash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.nikitagordia.chatme.R;
import com.nikitagordia.chatme.module.main.MainActivity;
import com.nikitagordia.chatme.module.signin.view.SigninActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) startActivity(new Intent(this, SigninActivity.class));
                                                        else startActivity(new Intent(this, MainActivity.class));

        finish();
    }
}
