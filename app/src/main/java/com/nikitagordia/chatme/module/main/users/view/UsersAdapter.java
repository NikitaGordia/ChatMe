package com.nikitagordia.chatme.module.main.users.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nikitagordia.chatme.R;
import com.nikitagordia.chatme.databinding.LayoutUserHolderBinding;
import com.nikitagordia.chatme.module.main.users.model.User;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by nikitagordia on 3/30/18.
 */

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserHolder>{

    private Context context;
    private List<User> list, filter;
    private String query;

    public UsersAdapter(Context context) {
        list = new LinkedList<>();
        filter = new LinkedList<>();
        query = "";
        this.context = context;
    }

    public void updateUser(List<User> users) {
        list.clear();
        list.addAll(users);
        refreshFilter();
    }

    public void refreshFilter() {
        filter.clear();
        for (int i = 0; i < list.size(); i++)
            if (valid(list.get(i).getName())) filter.add(list.get(i));
        notifyDataSetChanged();
    }

    public void setQuery(String query) {
        this.query = query;
        refreshFilter();
    }

    private boolean valid(String name) {
        if (query.isEmpty()) return true;
        return name.contains(query);
    }

    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserHolder(LayoutUserHolderBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(UserHolder holder, int position) {
        holder.bind(filter.get(position));
    }

    @Override
    public int getItemCount() {
        return filter.size();
    }

    public class UserHolder extends RecyclerView.ViewHolder {

        private LayoutUserHolderBinding bind;

        public UserHolder(LayoutUserHolderBinding bind) {
            super(bind.getRoot());
            this.bind = bind;
        }

        public void bind(User user) {
            bind.userName.setText(user.getName());
            bind.userEmail.setText(user.getEmail());
        }
    }
}
