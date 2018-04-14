package com.nikitagordia.chatme.module.main.users.view;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nikitagordia.chatme.R;
import com.nikitagordia.chatme.databinding.LayoutUserHolderBinding;
import com.nikitagordia.chatme.module.main.users.model.User;
import com.nikitagordia.chatme.module.profile.view.ProfileActivity;
import com.nikitagordia.chatme.utils.ImageUtils;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by nikitagordia on 3/30/18.
 */

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserHolder>{

    private Context context;
    private Activity activity;

    private List<User> list, filter;
    private String query;

    public UsersAdapter(Context context, Activity activity) {
        list = new LinkedList<>();
        filter = new LinkedList<>();
        query = "";
        this.context = context;
        this.activity = activity;
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
        this.query = query.toLowerCase();
        refreshFilter();
    }

    private boolean valid(String name) {
        if (query.isEmpty()) return true;
        return name.toLowerCase().contains(query);
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

    public class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private LayoutUserHolderBinding bind;

        private User user;

        public UserHolder(LayoutUserHolderBinding bind) {
            super(bind.getRoot());
            this.bind = bind;
            bind.body.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (user == null || user.getUid() == null || user.getUid().isEmpty()) return;
            context.startActivity(ProfileActivity.getIntent(user.getUid(), context), ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                    new Pair<View, String>(bind.userName, "nickname"),
                    new Pair<View, String>(bind.userEmail, "email"),
                    new Pair<View, String>(bind.photo, "photo")
            ).toBundle());
        }

        public void bind(User user) {
            this.user = user;
            bind.userName.setText(user.getName());
            bind.userEmail.setText(user.getEmail());
            if (user.getPhoto_url() != null) Picasso.with(context).load(user.getPhoto_url()).placeholder(R.drawable.user_photo_holder).resize(ImageUtils.SIZE_XXL, ImageUtils.SIZE_XXL).into(bind.photo);
        }
    }
}
