package com.nikitagordia.chatme.module.main;

import android.databinding.DataBindingUtil;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.nikitagordia.chatme.R;
import com.nikitagordia.chatme.databinding.ActivityMainBinding;
import com.nikitagordia.chatme.module.main.profile.view.FragmentPagerViewAdapter;
import com.nikitagordia.chatme.module.main.profile.view.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    private FragmentPagerAdapter adapter;
    private ActivityMainBinding bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = DataBindingUtil.setContentView(this, R.layout.activity_main);

        adapter = new FragmentPagerViewAdapter(getSupportFragmentManager(), new Fragment[]{
                new ProfileFragment(),
                new ProfileFragment(),
                new ProfileFragment()
        }, new String[] {
                getResources().getString(R.string.home),
                getResources().getString(R.string.home),
                getResources().getString(R.string.home)
        });

        bind.viewPager.setAdapter(adapter);
        bind.viewPager.setOffscreenPageLimit(3);

        bind.tabLayout.setupWithViewPager(bind.viewPager);
    }
}
