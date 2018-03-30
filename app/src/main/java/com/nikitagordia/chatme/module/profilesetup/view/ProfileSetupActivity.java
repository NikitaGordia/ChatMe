package com.nikitagordia.chatme.module.profilesetup.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.nikitagordia.chatme.R;
import com.nikitagordia.chatme.databinding.ActivityProfileSetupBinding;
import com.nikitagordia.chatme.module.main.MainActivity;

public class ProfileSetupActivity extends AppCompatActivity {

    public static final int PROFILE_SETUP_WITH_GOOGLE = 0;
    public static final int PROFILE_SETUP_WITH_TWITTER = 1;
    public static final int PROFILE_SETUP_WITH_PHONE = 2;
    public static final int PROFILE_SETUP_WITH_FACEBOOK = 3;
    public static final int PROFILE_SETUP_WITH_EMAIL_AND_PASSWORD = 4;

    public static final String EXTRA_SETUP_METHOD = "com.nikitagordia.chatme.module.profilesetup.view.ProfileSetupActivity.method";

    private ActivityProfileSetupBinding bind;

    private FirebaseAuth auth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = DataBindingUtil.setContentView(this, R.layout.activity_profile_setup);

        switch (getIntent().getIntExtra(EXTRA_SETUP_METHOD, -1)) {
            case PROFILE_SETUP_WITH_GOOGLE : bind.method.setImageResource(R.drawable.logo_google); break;
            case PROFILE_SETUP_WITH_FACEBOOK : bind.method.setImageResource(R.drawable.logo_facebook); break;
            case PROFILE_SETUP_WITH_TWITTER : bind.method.setImageResource(R.drawable.logo_twitter); break;
            case PROFILE_SETUP_WITH_PHONE : bind.method.setImageResource(R.drawable.logo_phone); break;
            default: PROFILE_SETUP_WITH_EMAIL_AND_PASSWORD : bind.method.setImageResource(R.drawable.logo_login);
        }

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            if (user.getEmail() != null) bind.info.setText(user.getEmail()); else
                if (user.getPhoneNumber() != null) bind.info.setText(user.getPhoneNumber());
            if (user.getDisplayName() != null) bind.nickname.setText(user.getDisplayName());
        } else finish();

        bind.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = bind.nickname.getText().toString();
                FirebaseUser user = auth.getCurrentUser();
                if (nickname.isEmpty()) {
                    showToast(R.string.empty_field);
                    return;
                }
                database.getReference().child("user").child(user.getUid()).child("name").setValue(nickname);
                String email = auth.getCurrentUser().getEmail();
                if (email == null) email = auth.getCurrentUser().getPhoneNumber();
                database.getReference().child("user").child(user.getUid()).child("email").setValue(email);
                startActivity(new Intent(ProfileSetupActivity.this, MainActivity.class));
                finish();
            }
        });

    }

    public void showToast(@StringRes int res) {
        Toast.makeText(this, getResources().getString(res), Toast.LENGTH_SHORT).show();
    }
}
