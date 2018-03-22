package com.nikitagordia.chatme.module.signin.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AtomicFile;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.nikitagordia.chatme.R;
import com.nikitagordia.chatme.databinding.ActivitySigninBinding;
import com.nikitagordia.chatme.module.profile.view.ProfileActivity;
import com.nikitagordia.chatme.utils.Const;

import java.util.Arrays;

public class SigninActivity extends AppCompatActivity {

    private static final int GOOGLE_REQUEST_CODE = 1;

    private ActivitySigninBinding bind;

    private FirebaseAuth auth;
    private GoogleApiClient client;
    private ProgressDialog dialog;
    private LoginManager loginManager;
    private CallbackManager callbackManager;

    private OnCompleteListener<AuthResult> signInCallback;


    private boolean isAnimated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bind = DataBindingUtil.setContentView(this, R.layout.activity_signin);

        auth = FirebaseAuth.getInstance();

        isAnimated = false;

        dialog = new ProgressDialog(this);
        dialog.setMessage(getResources().getString(R.string.loading));

        signInCallback = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                dialog.cancel();
                if (task.isSuccessful()) {
                    Toast.makeText(SigninActivity.this, getResources().getString(R.string.welcome), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SigninActivity.this, ProfileActivity.class));
                    finish();
                } else {
                    Toast.makeText(SigninActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                    bind.password.setText("");
                }
            }
        };

        bind.center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isAnimated) return;
                isAnimated = true;

                showLoginText();

                showCreateText();

                hideTapHere();

                double r = 3d * Math.min(bind.area.getMeasuredWidth(), bind.area.getMeasuredHeight()) / 8d;
                double centerX = bind.area.getMeasuredWidth() / 2d;
                double centerY = bind.area.getMeasuredHeight() / 2d;

                showAnimation(bind.phone,
                        (float)(centerX + r * Math.cos(Const.PHONE_ANGLE) - bind.phone.getMeasuredWidth() / 2d),
                        (float)(centerY - r * Math.sin(Const.PHONE_ANGLE) - bind.phone.getMeasuredHeight() / 2d),
                        Const.PHONE_SHOW_DURAION);

                showAnimation(bind.github,
                        (float)(centerX + r * Math.cos(Const.GITHUB_ANGLE) - bind.github.getMeasuredWidth() / 2d),
                        (float)(centerY - r * Math.sin(Const.GITHUB_ANGLE) - bind.github.getMeasuredHeight() / 2d),
                        Const.GITHUB_SHOW_DURATION);

                showAnimation(bind.twitter,
                        (float)(centerX + r * Math.cos(Const.TWITTER_ANGLE) - bind.twitter.getMeasuredWidth() / 2d),
                        (float)(centerY - r * Math.sin(Const.TWITTER_ANGLE) - bind.twitter.getMeasuredHeight() / 2d),
                        Const.TWITTER_SHOW_DURATION);

                showAnimation(bind.google,
                        (float)(centerX + r * Math.cos(Const.GOOGLE_ANGLE) - bind.google.getMeasuredWidth() / 2d),
                        (float)(centerY - r * Math.sin(Const.GOOGLE_ANGLE) - bind.google.getMeasuredHeight() / 2d),
                        Const.GOOGLE_SHOW_DURAION);

                showAnimation(bind.facebook,
                        (float)(centerX + r * Math.cos(Const.FACEBOOK_ANGLE) - bind.facebook.getMeasuredWidth() / 2d),
                        (float)(centerY - r * Math.sin(Const.FACEBOOK_ANGLE) - bind.facebook.getMeasuredHeight() / 2d),
                        Const.FACEBOOK_SHOW_DURATION);

                showAnimation(bind.login,
                        bind.area.getMeasuredWidth() / 2 - bind.login.getMeasuredWidth() / 2,
                        3 * bind.area.getMeasuredHeight() / 4 - bind.login.getMeasuredHeight() / 2,
                        Const.LOGIN_SHOW_DURATION);
            }
        });

        bind.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bind.email.getText().toString().isEmpty() || bind.password.getText().toString().isEmpty()) {
                    Toast.makeText(SigninActivity.this, getResources().getString(R.string.empty_field), Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.createUserWithEmailAndPassword(bind.email.getText().toString(), bind.password.getText().toString()).addOnCompleteListener(signInCallback);
                dialog.show();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        client = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        dialog.cancel();
                        Toast.makeText(SigninActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        bind.google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(client), GOOGLE_REQUEST_CODE);
            }
        });

        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                auth.signInWithCredential(FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken()))
                        .addOnCompleteListener(signInCallback);
            }

            @Override
            public void onCancel() {
                dialog.cancel();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(SigninActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        bind.facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginManager.logInWithReadPermissions(SigninActivity.this, Arrays.asList("email", "public_profile"));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        dialog.show();
        if (requestCode == GOOGLE_REQUEST_CODE) {
            AuthCredential credential = GoogleAuthProvider.getCredential(Auth.GoogleSignInApi.getSignInResultFromIntent(data).getSignInAccount().getIdToken(), null);
            auth.signInWithCredential(credential).addOnCompleteListener(signInCallback);
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void hideTapHere() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(bind.tapHere, "alpha", 1f, 0);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.start();
    }

    private void showLoginText() {
        bind.loginTv.setX(bind.area.getMeasuredWidth() / 2 - bind.loginTv.getMeasuredWidth() / 2);
        bind.loginTv.setY(3 * bind.area.getMeasuredHeight() / 4 + bind.login.getMeasuredHeight() / 2 + bind.loginTv.getMeasuredHeight() / 2);
        bind.loginTv.setVisibility(View.VISIBLE);
        ObjectAnimator animator = ObjectAnimator.ofFloat(bind.loginTv, "alpha", 0f, 1f).setDuration(Const.LOGIN_SHOW_DURATION);
        ObjectAnimator animatorScaleX = ObjectAnimator.ofFloat(bind.loginTv, "scaleX", 0.7f, 1f).setDuration(Const.LOGIN_SHOW_DURATION);
        ObjectAnimator animatorScaleY = ObjectAnimator.ofFloat(bind.loginTv, "scaleY", 0.7f, 1f).setDuration(Const.LOGIN_SHOW_DURATION);
        animator.setInterpolator(new AccelerateInterpolator());
        animatorScaleX.setInterpolator(new DecelerateInterpolator());
        animatorScaleY.setInterpolator(new DecelerateInterpolator());
        animator.start();
        animatorScaleX.start();
        animatorScaleY.start();
    }

    private void showCreateText() {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(bind.create, "scaleX", 1f, 0.8f).setDuration(Const.FACEBOOK_SHOW_DURATION);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(bind.create, "scaleY", 1f, 0.8f).setDuration(Const.FACEBOOK_SHOW_DURATION);
        animatorX.setInterpolator(new DecelerateInterpolator());
        animatorY.setInterpolator(new DecelerateInterpolator());
        animatorX.start();
        animatorY.start();
    }

    private void showAnimation(View v, float x, float y, int duration) {
        int fromY = bind.area.getMeasuredHeight();

        v.setX(x);
        v.setY(fromY);
        v.setVisibility(View.VISIBLE);

        AnimatorSet set = new AnimatorSet();

        ObjectAnimator animatorHeight = ObjectAnimator.ofFloat(v, "y", fromY, y).setDuration(duration);
        animatorHeight.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator animatorScaleX = ObjectAnimator.ofFloat(v, "scaleX", 0.8f, 1f).setDuration(duration);
        ObjectAnimator animatorScaleY = ObjectAnimator.ofFloat(v, "scaleY", 0.8f, 1f).setDuration(duration);

        set.play(animatorHeight)
                .with(animatorScaleX)
                .with(animatorScaleY);

        set.start();
    }
}