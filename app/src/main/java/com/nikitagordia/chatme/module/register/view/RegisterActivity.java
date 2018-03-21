package com.nikitagordia.chatme.module.register.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.nikitagordia.chatme.R;
import com.nikitagordia.chatme.databinding.ActivityRegisterBinding;
import com.nikitagordia.chatme.utils.Const;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bind = DataBindingUtil.setContentView(this, R.layout.activity_register);

        bind.create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showLoginText();

                showCreateText();

                showAnimation(bind.phone,
                        bind.area.getMeasuredWidth() / 2 - bind.phone.getMeasuredWidth() / 2,
                        bind.area.getMeasuredHeight() / 4 - bind.phone.getMeasuredHeight() / 2,
                        Const.PHONE_SHOW_DURAION);

                showAnimation(bind.github,
                        bind.area.getMeasuredWidth() / 3 - bind.github.getMeasuredWidth(),
                        bind.area.getMeasuredHeight() / 3 - bind.github.getMeasuredHeight() / 2,
                        Const.GITHUB_SHOW_DURATION);

                showAnimation(bind.twitter,
                        3 * bind.area.getMeasuredWidth() / 4,
                        bind.area.getMeasuredHeight() / 2 - bind.twitter.getMeasuredHeight() / 2,
                        Const.TWITTER_SHOW_DURATION);

                showAnimation(bind.google,
                        bind.area.getMeasuredWidth() / 4 - bind.google.getMeasuredWidth(),
                        bind.area.getMeasuredHeight() / 2 - bind.google.getMeasuredHeight() / 2,
                        Const.GOOGLE_SHOW_DURAION);

                showAnimation(bind.facebook,
                        2 * bind.area.getMeasuredWidth() / 3,
                        bind.area.getMeasuredHeight() / 3 - bind.github.getMeasuredHeight() / 2,
                        Const.FACEBOOK_SHOW_DURATION);

                showAnimation(bind.login,
                        bind.area.getMeasuredWidth() / 2 - bind.login.getMeasuredWidth() / 2,
                        3 * bind.area.getMeasuredHeight() / 4 - bind.login.getMeasuredHeight() / 2,
                        Const.LOGIN_SHOW_DURATION);
            }
        });
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

    private void showAnimation(View v, int x, int y, int duration) {
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