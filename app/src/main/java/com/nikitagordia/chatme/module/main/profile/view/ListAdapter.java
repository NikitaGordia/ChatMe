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
    private RecyclerView view;

    public ListAdapter(Context context, RecyclerView view) {
        list = new ArrayList<>();
        this.context = context;
        this.view = view;
    }

    @Override
    public PostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PostHolder(LayoutBlogPostBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(PostHolder holder, int position) {
        holder.bindData(list.get(position));
    }

    public void addPost(BlogPost post) {
        view.scrollToPosition(0);
        list.add(0, post);
        notifyItemInserted(0);
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
