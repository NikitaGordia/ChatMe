package com.nikitagordia.chatme.module.main.profile.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nikitagordia.chatme.R;
import com.nikitagordia.chatme.databinding.LayoutBlogPostBinding;
import com.nikitagordia.chatme.module.main.profile.model.BlogPost;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by nikitagordia on 3/28/18.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.PostHolder> {

    private List<BlogPost> list;
    private Context context;

    public ListAdapter(Context context) {
        list = new ArrayList<>();
        this.context = context;
    }

    @Override
    public PostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PostHolder(LayoutBlogPostBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(PostHolder holder, int position) {
        holder.bindData(list.get(position));
    }

    public void updatePosts(List<BlogPost> posts) {
        list.clear();
        list.addAll(posts);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {

        private LayoutBlogPostBinding bind;

        public PostHolder(LayoutBlogPostBinding bind) {
            super(bind.getRoot());
            this.bind = bind;
        }

        public void bindData(BlogPost post) {
            bind.nickname.setText(post.getOwner_name());
            bind.content.setText(post.getContent());
            bind.date.setText(post.getDate());
        }

    }
}
