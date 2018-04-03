package com.nikitagordia.chatme.module.main;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by nikitagordia on 3/28/18.
 */

public class FragmentPagerViewAdapter extends FragmentPagerAdapter {

    private Fragment[] list;
    private String[] labelList;

    public FragmentPagerViewAdapter(FragmentManager fm, Fragment[] list, String[] labelList) {
        super(fm);
        this.list = list;
        this.labelList = labelList;
    }

    @Override
    public Fragment getItem(int position) {
        return list[position];
    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return labelList[position];
    }
}
