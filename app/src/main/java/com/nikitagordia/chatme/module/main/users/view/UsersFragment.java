package com.nikitagordia.chatme.module.main.users.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nikitagordia.chatme.R;
import com.nikitagordia.chatme.databinding.FragmentUsersBinding;

/**
 * Created by nikitagordia on 3/30/18.
 */

public class UsersFragment extends Fragment {

    private FragmentUsersBinding bind;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_users, container, false);

        bind.userList.setLayoutManager(new LinearLayoutManager(getContext()));

        return bind.getRoot();
    }
}
