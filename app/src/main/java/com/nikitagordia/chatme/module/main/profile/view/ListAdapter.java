package com.nikitagordia.chatme.module.main.profile.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nikitagordia.chatme.R;
import com.nikitagordia.chatme.databinding.LayoutBlogPostBinding;
import com.nikitagordia.chatme.module.postdetail.view.PostDetail;
import com.nikitagordia.chatme.module.main.profile.model.BlogPost;
import com.nikitagordia.chatme.utils.UtilsManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikitagordia on 3/28/18.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.PostHolder> {

    private List<BlogPost> list;
    private Activity activity;
    private Context context;
    private RecyclerView view;

    public ListAdapter(Context context, Activity activity, RecyclerView view) {
        list = new ArrayList<>();
        this.activity = activity;
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

    public class PostHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private LayoutBlogPostBinding bind;
        private BlogPost post;

        public PostHolder(LayoutBlogPostBinding bind) {
            super(bind.getRoot());
            this.bind = bind;
            bind.content.setOnClickListener(this);
            bind.date.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            activity.startActivity(PostDetail.getIntent(context, post), ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                    new Pair<View, String>(bind.nickname, "nickname"),
                    new Pair<View, String>(bind.photo, "photo"),
                    new Pair<View, String>(bind.date, "date"),
                    new Pair<View, String>(bind.content, "content"),
                    new Pair<View, String>(bind.like, "like"),
                    new Pair<View, String>(bind.comment, "comment"),
                    new Pair<View, String>(bind.view, "view"),
                    new Pair<View, String>(bind.postBody, "post_body")).toBundle());
        }

        public void bindData(BlogPost post) {
            this.post = post;
            bind.nickname.setText(post.getOwner_name());
            bind.content.setText(UtilsManager.cut(post.getContent(), 500));
            bind.date.setText(post.getDate());
            bind.like.setText(context.getResources().getString(R.string.like_cnt, post.getLike()));
            bind.comment.setText(context.getResources().getString(R.string.comment_cnt, post.getComment()));
            bind.view.setText(context.getResources().getString(R.string.view_cnt, post.getView()));
        }

    }
}
