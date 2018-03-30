package com.nikitagordia.chatme.module.main;

import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nikitagordia.chatme.R;
import com.nikitagordia.chatme.databinding.ActivityMainBinding;
import com.nikitagordia.chatme.module.main.profile.view.ProfileFragment;
import com.nikitagordia.chatme.module.main.users.view.UsersFragment;

public class MainActivity extends AppCompatActivity {

    private FragmentPagerAdapter adapter;
    private ActivityMainBinding bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = DataBindingUtil.setContentView(this, R.layout.activity_main);

        adapter = new FragmentPagerViewAdapter(getSupportFragmentManager(), new Fragment[]{
                new ProfileFragment(),
                new UsersFragment()
        }, new String[] {
                getResources().getString(R.string.home),
                "Users"
        });

        bind.viewPager.setAdapter(adapter);
        bind.viewPager.setOffscreenPageLimit(2);

        bind.tabLayout.setupWithViewPager(bind.viewPager);
    }
}
