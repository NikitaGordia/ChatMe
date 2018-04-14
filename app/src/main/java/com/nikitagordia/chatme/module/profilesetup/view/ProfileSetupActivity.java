package com.nikitagordia.chatme.module.profilesetup.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.nikitagordia.chatme.R;
import com.nikitagordia.chatme.databinding.ActivityProfileSetupBinding;
import com.nikitagordia.chatme.module.main.MainActivity;
import com.nikitagordia.chatme.module.signin.view.PhotoPickDialog;
import com.nikitagordia.chatme.utils.ImageUtils;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ProfileSetupActivity extends AppCompatActivity {

    public static final int PROFILE_SETUP_WITH_GOOGLE = 0;
    public static final int PROFILE_SETUP_WITH_TWITTER = 1;
    public static final int PROFILE_SETUP_WITH_PHONE = 2;
    public static final int PROFILE_SETUP_WITH_FACEBOOK = 3;
    public static final int PROFILE_SETUP_WITH_EMAIL_AND_PASSWORD = 4;

    public static final int REQUEST_GALLERY_PICK = 4;
    public static final int REQUEST_CAMERA_PICK = 5;

    public static final String EXTRA_SETUP_METHOD = "com.nikitagordia.chatme.module.profilesetup.view.ProfileSetupActivity.method";

    private ActivityProfileSetupBinding bind;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private FirebaseStorage storage;

    private PhotoPickDialog dialog;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = DataBindingUtil.setContentView(this, R.layout.activity_profile_setup);

        boolean edit = false;

        switch (getIntent().getIntExtra(EXTRA_SETUP_METHOD, -1)) {
            case PROFILE_SETUP_WITH_GOOGLE :
                bind.methodImg.setImageResource(R.drawable.logo_google);
                bind.methodTv.setText(R.string.google);
                break;
            case PROFILE_SETUP_WITH_FACEBOOK :
                bind.methodImg.setImageResource(R.drawable.logo_facebook);
                bind.methodTv.setText(R.string.facebook);
                break;
            case PROFILE_SETUP_WITH_TWITTER :
                bind.methodImg.setImageResource(R.drawable.logo_twitter);
                bind.methodTv.setText(R.string.twitter);
                break;
            case PROFILE_SETUP_WITH_PHONE :
                bind.methodImg.setImageResource(R.drawable.logo_phone);
                bind.methodTv.setText(R.string.phone_sms);
                break;
            case PROFILE_SETUP_WITH_EMAIL_AND_PASSWORD :
                bind.methodImg.setImageResource(R.drawable.logo_login);
                bind.methodTv.setText(R.string.email_and_password);
                break;
            default:
                edit = true;
                bind.methodImg.setImageResource(R.drawable.icon_profile_setup);
                bind.methodTv.setText(R.string.profile_setup);
                break;
        }

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            if (user.getEmail() != null) bind.info.setText(user.getEmail()); else
                if (user.getPhoneNumber() != null) bind.info.setText(user.getPhoneNumber());
            if (user.getDisplayName() != null) bind.nickname.setText(user.getDisplayName());
            if (user.getPhotoUrl() != null) {
                if (edit) {
                    Picasso.with(this).load(user.getPhotoUrl()).into(bind.photo);
                } else {
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                            .setPhotoUri(null)
                            .build();
                    user.updateProfile(profileChangeRequest);
                }
            }
        } else finish();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.loading));

        bind.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = bind.nickname.getText().toString();
                FirebaseUser user = auth.getCurrentUser();
                if (nickname.isEmpty()) {
                    showToast(R.string.empty_field);
                    return;
                }
                UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                        .setDisplayName(nickname)
                        .build();
                user.updateProfile(profileChangeRequest);

                database.getReference().child("user").child(user.getUid()).child("name").setValue(nickname);
                String email = auth.getCurrentUser().getEmail();
                if (email == null) email = auth.getCurrentUser().getPhoneNumber();
                database.getReference().child("user").child(user.getUid()).child("email").setValue(email);
                startActivity(new Intent(ProfileSetupActivity.this, MainActivity.class));
                finish();
            }
        });

        dialog = new PhotoPickDialog();

        bind.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show(getSupportFragmentManager(), "mytg");
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY_PICK && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                updatePhoto(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == REQUEST_CAMERA_PICK && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            updatePhoto(imageBitmap);
        }
    }

    private void updatePhoto(Bitmap bitmap) {
        bind.photo.setImageBitmap(bitmap);

        Bitmap result = ImageUtils.getCircularBitmap(bitmap);
        bind.photo.setImageBitmap(result);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        result.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        progressDialog.show();
        dialog.dismiss();
        storage.getReference().child("user").child(auth.getCurrentUser().getUid()).putBytes(data).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    database.getReference().child("user").child(auth.getCurrentUser().getUid()).child("photo_url").setValue(task.getResult().getDownloadUrl().toString());
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setPhotoUri(task.getResult().getDownloadUrl())
                        .build();
                    auth.getCurrentUser().updateProfile(profileUpdates);
                } else {
                    Toast.makeText(ProfileSetupActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                }
                progressDialog.cancel();
            }
        });
    }

    public void showToast(@StringRes int res) {
        Toast.makeText(this, getResources().getString(res), Toast.LENGTH_SHORT).show();
    }
}
