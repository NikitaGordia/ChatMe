package com.nikitagordia.chatme.module.main;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.nikitagordia.chatme.R;
import com.nikitagordia.chatme.databinding.ActivityMainBinding;
import com.nikitagordia.chatme.module.main.chats.view.ChatsFragment;
import com.nikitagordia.chatme.module.main.profile.view.ProfileFragment;
import com.nikitagordia.chatme.module.main.users.view.UsersFragment;
import com.nikitagordia.chatme.module.postdetail.view.PostDetailActivity;

public class MainActivity extends AppCompatActivity {

    private FragmentPagerAdapter adapter;
    private ActivityMainBinding bind;

    private Fragment[] list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = DataBindingUtil.setContentView(this, R.layout.activity_main);

        list = new Fragment[]{
                new ProfileFragment(),
                new ChatsFragment(),
                new UsersFragment()
        };

        adapter = new FragmentPagerViewAdapter(getSupportFragmentManager(), list, new String[] {
                getResources().getString(R.string.home),
                getResources().getString(R.string.chats),
                getResources().getString(R.string.users)
        });

        bind.viewPager.setAdapter(adapter);
        bind.viewPager.setOffscreenPageLimit(3);

        bind.tabLayout.setupWithViewPager(bind.viewPager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            ((ProfileFragment)list[0]).updatePost(
                    data.getStringExtra(PostDetailActivity.EXTRA_ID),
                    data.getLongExtra(PostDetailActivity.EXTRA_LIKE, 0),
                    data.getLongExtra(PostDetailActivity.EXTRA_COMMENT, 0),
                    data.getLongExtra(PostDetailActivity.EXTRA_VIEW, 0)
            );
        }
    }
}
